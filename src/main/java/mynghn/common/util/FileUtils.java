package mynghn.common.util;

import java.util.Objects;
import mynghn.App;

public class FileUtils {

    public static String getResourceFullPath(String resourcePath) {
        return Objects.requireNonNull(App.class.getResource(resourcePath)).getFile();
    }
}
