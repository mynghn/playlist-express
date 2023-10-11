package mynghn.youtube.enums;

import lombok.Getter;

@Getter
public enum Topic {
    MUSIC("/m/04rlf");

    private final String id;

    Topic(String id) {
        this.id = id;
    }
}
