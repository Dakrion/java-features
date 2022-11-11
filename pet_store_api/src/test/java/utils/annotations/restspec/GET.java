package utils.annotations.restspec;

import java.lang.annotation.*;

/**
 * Marker for rest method GET
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface GET {

    /**
     * Endpoint for method
     * @return String
     */
    String endpoint();
}
