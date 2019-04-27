package info.ivicel.springflasky.core;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;


/**
 * mark an entity field to sanitize html tag
 * @see info.ivicel.springflasky.core.listener.SanitizerListener
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Sanitize {

}
