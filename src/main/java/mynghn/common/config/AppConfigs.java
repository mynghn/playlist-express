package mynghn.common.config;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Objects;
import java.util.Properties;
import lombok.RequiredArgsConstructor;
import mynghn.common.exception.FileReadException;

@RequiredArgsConstructor
public class AppConfigs {

    private final Properties configs = new Properties(DefaultAppConfigs.build());
    private final String configFilePath;

    public AppConfigs() {
        this(buildDefaultConfigFilePath());
    }

    private static String buildDefaultConfigFilePath() {
        return Objects.requireNonNull(AppConfigs.class.getResource("/app.properties")).getPath();
    }

    public String get(String configKey) {
        if (configs.isEmpty()) {
            try {
                loadConfigs(); // lazy load configs from file
            } catch (IOException e) {
                throw new FileReadException(
                        "IOException occurred while reading application config file.", e);
            }
        }
        return configs.getProperty(configKey);
    }

    private void loadConfigs() throws IOException {
        try (Reader reader = new BufferedReader(new FileReader(configFilePath))) {
            configs.load(reader);
        } catch (FileNotFoundException e) {
            throw new IllegalStateException("Application configuration file not found.", e);
        }
    }
}
