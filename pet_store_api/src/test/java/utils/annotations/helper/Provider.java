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
     * name of test method, which use this provider
     * @return String
     */
    String testMethod() default "";

    /**
     * name of test class, which use this provider
     * @return String
     */
    String testClass() default "";

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