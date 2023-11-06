package mynghn.common.util;

import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.Optional;
import mynghn.common.exception.FileReadException;

public class JsonFileReader {

    public static <T> T read(String filePath, Class<T> deserializeInto) throws FileReadException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            return new Gson().fromJson(reader, deserializeInto);
        } catch (FileNotFoundException e) {
            throw new FileReadException(MessageFormat.format("File {0} not found.", filePath), e);
        } catch (IOException e) {
            throw new FileReadException(
                    MessageFormat.format("IOException occurred while reading file {0}.", filePath),
                    e);
        }
    }

    public static <T> Optional<T> readIfFound(String filePath, Class<T> deserializeInto) {
        try {
            return Optional.of(read(filePath, deserializeInto));
        } catch (FileReadException e) {
            if (e.getCause() instanceof FileNotFoundException) {
                return Optional.empty();
            }
            throw e;
        }
    }
}
