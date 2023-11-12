package mynghn.youtube.message;

import lombok.Getter;
import mynghn.youtube.enums.YouTubeResourceKind;

@Getter
public class YouTubeVideoId extends YouTubeResourceId {

    private final String videoId;

    public YouTubeVideoId(String videoId) {
        super(YouTubeResourceKind.VIDEO.getValue());
        this.videoId = videoId;
    }
}
