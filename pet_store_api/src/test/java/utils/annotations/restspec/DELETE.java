package utils.annotations.restspec;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
public @interface DELETE {

    String endpoint() default "";
}
