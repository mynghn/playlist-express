package mynghn.youtube.enums;

import lombok.Getter;

@Getter
public enum PlaylistPrivacyStatus {

    PRIVATE("private"), PUBLIC("public"), UNLISTED("unlisted");

    private final String value;

    PlaylistPrivacyStatus(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
