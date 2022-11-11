package utils.annotations.restspec;

import java.lang.annotation.*;

/**
 * Marker for rest method DELETE
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DELETE {

    /**
     * Endpoint for method
     * @return String
     */
    String endpoint();
}
