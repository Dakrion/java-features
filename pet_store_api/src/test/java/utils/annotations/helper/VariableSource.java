package utils.annotations.helper;

import org.junit.jupiter.params.provider.ArgumentsSource;
import tests.dataproviders.VariableArgumentsProvider;

import java.lang.annotation.*;

/**
 * Custom annotation for tests
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@ArgumentsSource(VariableArgumentsProvider.class)
public @interface VariableSource {

    /**
     * The name of the file
     * @return String
     */
    String fileName();

    /**
     *The name of the class, which use to cast
     * @return String
     */
    String className();
}
