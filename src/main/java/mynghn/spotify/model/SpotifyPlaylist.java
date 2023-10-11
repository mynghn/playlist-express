package mynghn.spotify.model;

import java.util.List;
import mynghn.spotify.message.retrieval.response.SpotifyPlaylistRetrievalResponse;

// TODO: Define properties
public record SpotifyPlaylist(List<Track> tracks) {

    public static SpotifyPlaylist fromResponse(SpotifyPlaylistRetrievalResponse response) {
        return new SpotifyPlaylist(List.of()); // FIXME: Fill w/ real data
    }
}
