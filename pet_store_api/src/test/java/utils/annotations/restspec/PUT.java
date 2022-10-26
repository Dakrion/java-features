package utils.annotations.restspec;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Marker for rest method PUT
 */
@Target(ElementType.METHOD)
public @interface PUT {

    /**
     * Endpoint for method
     * @return String
     */
    String endpoint() default "";
}
