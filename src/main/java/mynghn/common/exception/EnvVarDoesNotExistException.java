package mynghn.common.exception;

import java.text.MessageFormat;

public class EnvVarDoesNotExistException extends RuntimeException{

    public static String buildDefaultMessage(String varName) {
        return MessageFormat.format("Environment variable {0} does not exist.", varName);
    }

    public static EnvVarDoesNotExistException of(String varName) {
        return new EnvVarDoesNotExistException(buildDefaultMessage(varName));
    }

    public EnvVarDoesNotExistException(String message) {
        super(message);
    }
}
