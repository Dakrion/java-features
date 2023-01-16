package annotations;

import java.lang.annotation.*;

/**
 * Аннотация со свойствами конверта
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface EnvelopeProperties {

    /**
     * Неймспейс
     * @return
     */
    String namespace();

    /**
     * URI неймспейса
     * @return
     */
    String namespaceURI();
}
