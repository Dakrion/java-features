package annotations;

import java.lang.annotation.*;

/**
 * Маркер для soapAction
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface SoapAction {

    String value() default "";
}
