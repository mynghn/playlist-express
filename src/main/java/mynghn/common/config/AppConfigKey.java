package mynghn.common.config;

public enum AppConfigKey {

    /**
     * Application configure key for Spotify API client credentials file path definition. <br/>
     * Configuration value should be in form of absolute path from resources root.
     */
    SPOTIFY_CREDENTIAL_PATH("credential.spotify.path"),

    /**
     * Application configure key for YouTube API client credentials file path definition. <br/>
     * Configuration value should be in form of absolute path from resources root.
     */
    YOUTUBE_CREDENTIAL_PATH("credential.youtube.path");

    private final String value;

    AppConfigKey(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
