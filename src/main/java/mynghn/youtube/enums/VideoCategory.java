package mynghn.youtube.enums;

import lombok.Getter;

@Getter
public enum VideoCategory {
    MUSIC("10");

    private final String id;


    VideoCategory(String id) {
        this.id = id;
    }
}
