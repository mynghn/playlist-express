package mynghn.spotify.util;

import java.util.stream.Collectors;
import mynghn.spotify.message.retrieval.response.SpotifyPlaylistItemRetrievalResponse;
import mynghn.spotify.message.retrieval.response.SpotifyPlaylistRetrievalResponse;
import mynghn.spotify.model.SpotifyPlaylist;
import mynghn.spotify.model.Track;

public class PlaylistConverter {

    public static SpotifyPlaylist fromResponse(SpotifyPlaylistRetrievalResponse response) {
        return SpotifyPlaylist.builder()
                .description(response.description())
                .id(response.id())
                .name(response.name())
                .tracks(response.tracks().items().stream()
                        .map(SpotifyPlaylistItemRetrievalResponse::track)
                        .map(item -> (Track) item)
                        .collect(Collectors.toList()))
                .build();
    }
}
