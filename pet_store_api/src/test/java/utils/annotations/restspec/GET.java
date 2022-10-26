package utils.annotations.restspec;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Marker for rest method GET
 */
@Target(ElementType.METHOD)
public @interface GET {

    /**
     * Endpoint for method
     * @return String
     */
    String endpoint() default "";
}
