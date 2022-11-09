package tests.dataproviders;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.support.AnnotationConsumer;
import utils.annotations.helper.VariableSource;

import java.util.stream.Stream;

/**
 * Custom provider. Implements JUnit interfaces - {@link org.junit.jupiter.params.provider.ArgumentsProvider}, {@link org.junit.jupiter.params.support.AnnotationConsumer}
 */
public class VariableArgumentsProvider implements ArgumentsProvider, AnnotationConsumer<VariableSource> {

    private String pathFile;

    private String className;

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
        Stream<Arguments> value = null;
        try {
            value = ArgumentUtil.getArgumentFromFile(pathFile, className);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return value;
    }

    @Override
    public void accept(VariableSource variableSource) {
        pathFile = variableSource.fileName();
        className = variableSource.className();
    }
}
