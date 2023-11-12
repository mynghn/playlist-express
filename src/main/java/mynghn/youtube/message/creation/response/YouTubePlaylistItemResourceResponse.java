package mynghn.youtube.message.creation.response;

import java.time.LocalDateTime;
import mynghn.youtube.enums.PrivacyStatus;
import mynghn.youtube.message.YouTubeResourceId;

public record YouTubePlaylistItemResourceResponse(String id,
                                                  PlaylistItemSnippet snippet,
                                                  PlaylistItemStatus status,
                                                  PlaylistItemContentDetails contentDetails) {

    public record PlaylistItemSnippet(LocalDateTime publishedAt,
                               String title,
                               String description,
                               String channelId,
                               String channelTitle,
                               String videoOwnerChannelId,
                               String videoOwnerChannelTitle,
                               String playlistId,
                               int position,
                               YouTubeResourceId resourceId) {

    }

    public record PlaylistItemStatus(PrivacyStatus privacyStatus) {

    }

    public record PlaylistItemContentDetails(String videoId, LocalDateTime videoPublishedAt) {

    }
}
