package database.exceptions;

/**
 * Exception, выбрасываемое в результате конвертации результата запроса
 */
public class ConvertResultException extends RuntimeException {
    public ConvertResultException(String message) {super(message);}
}
