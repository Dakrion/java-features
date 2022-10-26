package utils.annotations.restspec;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Marker for rest method POST
 */
@Target(ElementType.METHOD)
public @interface POST {

    /**
     * Endpoint for method
     * @return String
     */
    String endpoint() default "";
}
