package annotations;

import java.lang.annotation.*;

@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(AttributesContainer.class)
public @interface Attribute {

    String name();

    String value();
}
