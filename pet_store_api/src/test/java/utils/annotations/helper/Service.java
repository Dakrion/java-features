package utils.annotations.helper;

import java.lang.annotation.*;

/**
 * Marker for service class
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Service {
    /**
     * name of service
     * @return String
     */
    String name();

    /**
     * Base url
     * @return String
     */
    String url();
}
