package mynghn.youtube.enums;

import lombok.Getter;

@Getter
public enum BaseUrl {
    OAUTH2("https://oauth2.googleapis.com"),
    YOUTUBE_DATA_API_V3("https://youtube.googleapis.com/youtube/v3");

    private final String value;

    BaseUrl(String value) {
        this.value = value;
    }
}
