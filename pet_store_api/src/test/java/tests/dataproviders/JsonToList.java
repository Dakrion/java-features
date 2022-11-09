package tests.dataproviders;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.params.aggregator.ArgumentsAccessor;
import org.junit.jupiter.params.aggregator.ArgumentsAggregationException;
import org.junit.jupiter.params.aggregator.ArgumentsAggregator;

import java.io.File;
import java.io.IOException;

/**
 * Aggregator for variable. Implements JUnit {@link org.junit.jupiter.params.aggregator.ArgumentsAggregator}
 */
public class JsonToList implements ArgumentsAggregator {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Object aggregateArguments(ArgumentsAccessor accessor, ParameterContext context) throws ArgumentsAggregationException {
        try {
            return objectMapper.readValue(new File(accessor.getString(0)), Class.forName(accessor.getString(1)).arrayType());
        } catch (IOException | ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        throw new NullPointerException("List is null!");
    }
}
