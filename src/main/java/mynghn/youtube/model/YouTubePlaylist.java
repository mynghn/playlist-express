package mynghn.youtube.model;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import mynghn.youtube.enums.PrivacyStatus;

@Builder
public record YouTubePlaylist(String id,
                              LocalDateTime publishedAt,
                              String title,
                              String description,
                              Channel channel,
                              PrivacyStatus privacyStatus,
                              List<YouTubePlaylistItem> items) {

}
