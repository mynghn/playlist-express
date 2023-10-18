package mynghn.youtube.message.search.response;

import lombok.Getter;

@Getter
public class YouTubeVideoId extends YouTubeSearchResultId {

    private final String videoId;

    public YouTubeVideoId(String videoId) {
        super(YouTubeSearchResultKind.VIDEO.getValue());
        this.videoId = videoId;
    }
}
