package utils.annotations.helper;

import java.lang.annotation.*;


/**
 * Annotation for provider methods
 */
@Documented
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
     * version of tests - positive, negative, unknown
     * @return enum
     */
    TestMode testMode() default TestMode.UNKNOWN;

    enum TestMode {
        POSITIVE,
        NEGATIVE,
        UNKNOWN
    }
}