package utils.annotations.helper;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Name of service
 */
@Target(ElementType.TYPE)
public @interface Service {

    String value();
}
