package annotations;

import java.lang.annotation.*;

/**
 * Контейнер для атрибутов
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AttributesContainer {

    Attribute[] value();
}
