package mynghn.youtube.message.creation.response;

import java.time.LocalDateTime;
import mynghn.youtube.enums.PrivacyStatus;

public record YouTubePlaylistResourceResponse(String id,
                                              PlaylistSnippet snippet,
                                              PlaylistStatus status,
                                              PlaylistContentDetails contentDetails) {

    public record PlaylistSnippet(LocalDateTime publishedAt,
                           String title,
                           String description,
                           String channelId,
                           String channelTitle) {

    }

    public record PlaylistStatus(PrivacyStatus privacyStatus) {

    }

    public record PlaylistContentDetails(int itemCount) {

    }
}
