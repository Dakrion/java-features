package utils.annotations.restspec;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Marker for rest method DELETE
 */
@Target(ElementType.METHOD)
public @interface DELETE {

    /**
     * Endpoint for method
     * @return String
     */
    String endpoint() default "";
}
