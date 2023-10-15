package mynghn.common.credential;

import java.text.MessageFormat;

public class EnvVarDoesNotExistException extends RuntimeException{

    public static String buildDefaultMessage(String envVarName) {
        return MessageFormat.format("Environment variable {0} does not exist.", envVarName);
    }

    public static EnvVarDoesNotExistException of(String varName) {
        return new EnvVarDoesNotExistException(buildDefaultMessage(varName));
    }

    public EnvVarDoesNotExistException(String message) {
        super(message);
    }
}
