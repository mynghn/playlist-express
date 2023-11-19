package mynghn.common.config;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.net.URL;
import java.text.MessageFormat;
import java.util.Properties;
import lombok.RequiredArgsConstructor;
import mynghn.common.exception.FileReadException;

@RequiredArgsConstructor
public class AppConfigs {

    private static final String DEFAULT_ABSOLUTE_CONFIG_FILE_PATH_FROM_RESOURCES_ROOT = "/app.properties";

    private final Properties configs = new Properties(DefaultAppConfigs.build());
    private final String configFilePath;

    public AppConfigs() {
        configFilePath = null;
    }

    private static String getDefaultConfigFilePath() throws FileNotFoundException {
        final URL configUrlFromResources = AppConfigs.class.getResource(
                DEFAULT_ABSOLUTE_CONFIG_FILE_PATH_FROM_RESOURCES_ROOT);
        if (configUrlFromResources == null) {
            throw new FileNotFoundException(MessageFormat.format(
                    "Application configuration file not found at {0} from resources root.",
                    DEFAULT_ABSOLUTE_CONFIG_FILE_PATH_FROM_RESOURCES_ROOT));
        }
        return configUrlFromResources.getPath();
    }

    public String get(AppConfigKey configKey) {
        if (configs.isEmpty()) {
            try {
                loadConfigs(); // lazy load configs from file
            } catch (FileNotFoundException e) {
                // pass
            } catch (IOException e) {
                throw new FileReadException(
                        "IOException occurred while reading application config file.", e);
            }
        }

        String configVal = configs.getProperty(configKey.toString());
        if (configVal == null) {
            throw new IllegalStateException(MessageFormat.format(
                    "Configuration with key \"{0}\" not found.\nAvailable configurations are:\n{1}",
                    configKey, this));
        }

        return configVal;
    }

    private void loadConfigs() throws IOException {
        final String filePath =
                configFilePath != null ? configFilePath : getDefaultConfigFilePath();
        try (Reader reader = new BufferedReader(new FileReader(filePath))) {
            configs.load(reader);
        }
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder(100);

        // open
        builder.append("{\n");

        // iterate over configs
        final var flagContainer = new Object() {
            boolean hasPrev = false;
        };
        configs.stringPropertyNames().forEach((configKey) -> {
            if (flagContainer.hasPrev) {
                builder.append(",\n");
            }

            builder.append(
                    String.format("\t\"%s\": \"%s\"", configKey, configs.getProperty(configKey)));

            if (!flagContainer.hasPrev) {
                flagContainer.hasPrev = true;
            }
        });

        // close
        builder.append("\n}");

        return builder.toString();
    }
}
