package annotations;

import java.lang.annotation.*;

/**
 * Аннотация для дочернего элемента
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ChildElement {

    /**
     * Имя элемента-родителя
     * @return String
     */
    String parent();
}
