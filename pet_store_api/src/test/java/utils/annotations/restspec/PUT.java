package utils.annotations.restspec;

import java.lang.annotation.*;

/**
 * Marker for rest method PUT
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface PUT {

    /**
     * Endpoint for method
     * @return String
     */
    String endpoint();
}
