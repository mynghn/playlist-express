package mynghn.spotify.enums;

import lombok.Getter;

@Getter
public enum BaseUrl {
    AUTH("https://accounts.spotify.com"),
    WEB_API_V1("https://api.spotify.com/v1");

    private final String value;

    BaseUrl(String value) {
        this.value = value;
    }
}
