package tests.dataproviders;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.experimental.UtilityClass;
import org.junit.jupiter.params.provider.Arguments;

import java.io.File;
import java.io.IOException;
import java.util.stream.Stream;

/**
 * Class, which use to generate test arguments in {@link tests.dataproviders.VariableArgumentsProvider}
 */
@UtilityClass
public class ArgumentUtil {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static Stream<Arguments> getArgumentFromFile(String fileName, String classFile) {
        Stream<Arguments> argumentStream = null;
        try {
            Object[] array = (Object[]) objectMapper.readValue(new File("src/test/resources/" + fileName), Class.forName("[L" + classFile + ";"));
            for (Object request : array) {
                if (argumentStream == null) {
                    argumentStream = Stream.of(Arguments.of(request));
                } else argumentStream = Stream.concat(Stream.of(Arguments.of(request)), argumentStream);
            }
            return argumentStream;
        } catch (IOException | ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        throw new NullPointerException("List is null!");
    }
}
