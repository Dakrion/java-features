package annotations;

import java.lang.annotation.*;

/**
 * Маркер для элемента
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface SoapElement {

    /**
     * Неймспейс
     * @return
     */
    String namespace() default "";

    /**
     * Имя элемента
     * @return
     */
    String name();
}
