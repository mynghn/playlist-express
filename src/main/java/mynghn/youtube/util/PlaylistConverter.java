package mynghn.youtube.util;

import java.util.List;
import java.util.stream.Collectors;
import mynghn.youtube.message.creation.response.YouTubePlaylistItemResourceResponse;
import mynghn.youtube.message.creation.response.YouTubePlaylistResourceResponse;
import mynghn.youtube.message.creation.response.YouTubePlaylistResourceResponse.PlaylistSnippet;
import mynghn.youtube.model.Channel;
import mynghn.youtube.model.YouTubePlaylist;

public class PlaylistConverter {

    public static YouTubePlaylist fromResponse(YouTubePlaylistResourceResponse playlistResponse,
            List<YouTubePlaylistItemResourceResponse> playlistItemResponses) {
        PlaylistSnippet playlistSnippet = playlistResponse.snippet();
        return YouTubePlaylist.builder()
                .id(playlistResponse.id())
                .publishedAt(playlistSnippet.publishedAt())
                .title(playlistSnippet.title())
                .description(playlistSnippet.description())
                .channel(new Channel(playlistSnippet.channelId(), playlistSnippet.channelTitle()))
                .privacyStatus(playlistResponse.status().privacyStatus())
                .items(playlistItemResponses.stream()
                        .map(PlaylistItemConverter::fromResponse)
                        .collect(Collectors.toList()))
                .build();
    }
}
