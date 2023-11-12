package mynghn.common.util;

import mynghn.common.exception.EnvVarDoesNotExistException;

public class EnvVarReader {

    public static String read(String varName) throws EnvVarDoesNotExistException {
        String envVar = System.getenv(varName);
        if (envVar == null) {
            throw EnvVarDoesNotExistException.of(varName);
        }
        return envVar;
    }
}
