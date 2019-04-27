package info.ivicel.springflasky.core.validation;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class EqualsMatchValidator implements ConstraintValidator<EqualsMatch, Object> {
    private String firstFieldName;
    private String secondFieldName;

    @Override
    public void initialize(EqualsMatch constraintAnnotation) {
        firstFieldName = constraintAnnotation.first();
        secondFieldName = constraintAnnotation.second();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        String first = getField(value, firstFieldName);
        String second = getField(value, secondFieldName);

        if (first == null) {
            return second == null;
        }

        return first.equals(second);
    }

    private String getField(Object obj, String name) {
        try {
            PropertyDescriptor descriptor = new PropertyDescriptor(name, obj.getClass());
            Method method = descriptor.getReadMethod();
            return (String) method.invoke(obj);
        } catch (IntrospectionException | IllegalAccessException | InvocationTargetException e) {
            return null;
        }
    }
}
