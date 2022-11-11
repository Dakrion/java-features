package utils.annotations.restspec;

import java.lang.annotation.*;

/**
 * Marker for rest method POST
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface POST {

    /**
     * Endpoint for method
     * @return String
     */
    String endpoint();
}
