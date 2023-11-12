package mynghn.youtube.model;

import java.time.LocalDateTime;
import lombok.Builder;
import mynghn.youtube.message.YouTubeResourceId;

@Builder
public record YouTubePlaylistItem(String id,
                                  LocalDateTime publishedAt,
                                  String title,
                                  String description,
                                  String playlistId,
                                  Channel channel,
                                  Channel videoOwnerChannel,
                                  int position,
                                  YouTubeResourceId resourceId) {

}
