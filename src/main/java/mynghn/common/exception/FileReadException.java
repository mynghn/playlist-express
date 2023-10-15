package mynghn.common.exception;

import java.io.IOException;

public class FileReadException extends RuntimeException {

    public FileReadException(String message, IOException cause) {
        super(message, cause);
    }
}
