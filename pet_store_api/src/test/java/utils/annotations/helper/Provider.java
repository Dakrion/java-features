package utils.annotations.helper;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for provider methods
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Provider {

    /**
     * name of test suite
     * @return String
     */
    String suite() default "";

    /**
     * variants of tests - positive, negative, unknown
     * @return enum
     */
    TestMode testMode() default TestMode.UNKNOWN;

    enum TestMode {
        POSITIVE,
        NEGATIVE,
        UNKNOWN
    }
}