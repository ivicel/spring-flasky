package info.ivicel.springflasky.core.listener;


import info.ivicel.springflasky.core.Sanitize;
import info.ivicel.springflasky.util.CommonUtil;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import lombok.extern.slf4j.Slf4j;

/**
 * sanitize html tag when save entity to database
 */
@Slf4j
public class SanitizerListener {

    @PrePersist
    @PreUpdate
    public void sanitize(Object object) {
        try {
            Field[] fields = object.getClass().getDeclaredFields();
            for (Field field : fields) {
                if (field.getAnnotation(Sanitize.class) != null) {
                    log.error(field.getType().toString());
                    if (!CharSequence.class.isAssignableFrom(field.getType())) {
                        log.warn("Annotation @Sanitize only support string field.");
                        continue;
                    }

                    PropertyDescriptor descriptor = new PropertyDescriptor(field.getName(), object.getClass());
                    String text = (String) descriptor.getReadMethod().invoke(object);
                    text = CommonUtil.sanitize(text);
                    Method writeMethod = descriptor.getWriteMethod();
                    writeMethod.invoke(object, text);
                }
            }
        } catch (IntrospectionException | IllegalAccessException | InvocationTargetException e) {
            log.error("can't sanitize {}: {}", object, e.getMessage());
        }
    }
}
