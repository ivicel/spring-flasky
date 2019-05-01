package info.ivicel.springflasky.util;

import javax.annotation.PostConstruct;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class ContextUtil {

    private ApplicationContext context;

    private static ApplicationContext ctx;

    public ContextUtil(ApplicationContext context) {
        this.context = context;
    }

    @PostConstruct
    public void init() {
        ctx = context;
    }

    public static <T> T getBean(Class<T> requiredType) throws BeansException {
        return ctx.getBean(requiredType);
    }
}
