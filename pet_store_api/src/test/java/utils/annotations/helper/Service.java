package utils.annotations.helper;

import java.lang.annotation.*;

/**
 * Name of service
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Service {

    String value();
}
