package mynghn.youtube.message.search.response;

import java.text.MessageFormat;
import java.util.Objects;
import lombok.Getter;

@Getter
public enum YouTubeSearchResultKind {
    VIDEO("youtube#video"), CHANNEL("youtube#channel"), PLAYLIST("youtube#playlist");

    private final String value;

    YouTubeSearchResultKind(String value) {
        this.value = value;
    }

    public static YouTubeSearchResultKind of(String kind) {
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
