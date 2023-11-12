package mynghn.youtube.enums;

import java.text.MessageFormat;
import java.util.Objects;
import lombok.Getter;

@Getter
public enum YouTubeResourceKind {
    VIDEO("youtube#video"), CHANNEL("youtube#channel"), PLAYLIST("youtube#playlist");

    private final String value;

    YouTubeResourceKind(String value) {
        this.value = value;
    }

    public static YouTubeResourceKind of(String kind) {
        Objects.requireNonNull(kind);
        if (kind.equals(VIDEO.value)) {
            return VIDEO;
        }
        if (kind.equals(CHANNEL.value)) {
            return CHANNEL;
        }
        if (kind.equals(PLAYLIST.value)) {
            return PLAYLIST;
        }
        throw new IllegalArgumentException(
                MessageFormat.format("Undefined value {0} encountered.", kind));
    }
}
