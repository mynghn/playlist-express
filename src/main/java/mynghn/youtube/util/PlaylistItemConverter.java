package mynghn.youtube.util;

import mynghn.youtube.message.creation.response.YouTubePlaylistItemResourceResponse;
import mynghn.youtube.message.creation.response.YouTubePlaylistItemResourceResponse.PlaylistItemSnippet;
import mynghn.youtube.model.Channel;
import mynghn.youtube.model.YouTubePlaylistItem;

public class PlaylistItemConverter {

    public static YouTubePlaylistItem fromResponse(YouTubePlaylistItemResourceResponse response) {
        PlaylistItemSnippet snippet = response.snippet();
        return YouTubePlaylistItem.builder()
                .id(response.id())
                .publishedAt(snippet.publishedAt())
                .title(snippet.title())
                .description(snippet.description())
                .playlistId(snippet.playlistId())
                .channel(new Channel(snippet.channelId(), snippet.channelTitle()))
                .videoOwnerChannel(new Channel(snippet.videoOwnerChannelId(),
                        snippet.videoOwnerChannelTitle()))
                .position(snippet.position())
                .resourceId(snippet.resourceId())
                .build();
    }
}
