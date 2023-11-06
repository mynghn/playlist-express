package mynghn.common.util;

import java.util.Objects;

public class FileUtils {

    public static String getResourceFullPath(String resourcePath) {
        return Objects.requireNonNull(Object.class.getResource(resourcePath)).getFile();
    }
}
