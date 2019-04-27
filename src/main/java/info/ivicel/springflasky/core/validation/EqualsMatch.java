package info.ivicel.springflasky.core.validation;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

@Documented
@Retention(RUNTIME)
@Target(value = {TYPE, ANNOTATION_TYPE})
@Constraint(validatedBy = EqualsMatchValidator.class)
public @interface EqualsMatch {

    String first();

    String second();

    String message() default "{first} not equals {second}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};


    @Documented
    @Target(value = {TYPE, ANNOTATION_TYPE})
    @Retention(RUNTIME)
    @interface List {
        EqualsMatch[] value();
    }
}
