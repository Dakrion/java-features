package annotations;

import java.lang.annotation.*;

/**
 * Атрибут элемента
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(AttributesContainer.class)
public @interface Attribute {

    /**
     * Имя атрибута
     * @return String
     */
    String name();

    /**
     * Значение атрибута
     * @return String
     */
    String value();
}
