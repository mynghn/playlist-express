package mynghn.common.config;

import java.text.MessageFormat;
import java.util.AbstractMap.SimpleEntry;
import java.util.Map;
import java.util.Properties;
import lombok.Getter;

@Getter
public class DefaultAppConfigs {

    private static final Map<AppConfigKey, String> defaultConfigsMap = Map.ofEntries(
            new SimpleEntry<>(AppConfigKey.SPOTIFY_CREDENTIAL_PATH, "/credential/spotify/client_credentials.json"),
            new SimpleEntry<>(AppConfigKey.YOUTUBE_CREDENTIAL_PATH, "/credential/youtube/client_credentials.json")
    );

    public static String get(AppConfigKey key) {
        if (!defaultConfigsMap.containsKey(key)) {
            throw new IllegalArgumentException(
                    MessageFormat.format("{0} does not have default configuration defined.", key));
        }
        return defaultConfigsMap.get(key);
    }

    public static Properties build() {
        Properties defaultConfigs = new Properties();

        // spotify credentials path
        defaultConfigs.setProperty(AppConfigKey.SPOTIFY_CREDENTIAL_PATH.toString(),
                get(AppConfigKey.SPOTIFY_CREDENTIAL_PATH));

        // youtube credentials path
        defaultConfigs.setProperty(AppConfigKey.YOUTUBE_CREDENTIAL_PATH.toString(),
                get(AppConfigKey.YOUTUBE_CREDENTIAL_PATH));

        return defaultConfigs;
    }
}
