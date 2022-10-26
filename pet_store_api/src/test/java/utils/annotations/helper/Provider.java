package utils.annotations.helper;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
public @interface Provider {

    String suite() default "";

    TestMode testMode() default TestMode.UNKNOWN;

    enum TestMode {
        POSITIVE,
        NEGATIVE,
        UNKNOWN
    }
}