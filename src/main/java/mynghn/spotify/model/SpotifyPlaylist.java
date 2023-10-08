package mynghn.spotify.model;

import mynghn.spotify.message.retrieval.response.SpotifyPlaylistRetrievalResponse;

public record SpotifyPlaylist() {

    // TODO: Define properties

    public static SpotifyPlaylist fromResponse(SpotifyPlaylistRetrievalResponse response) {
        return new SpotifyPlaylist();
    }
}
