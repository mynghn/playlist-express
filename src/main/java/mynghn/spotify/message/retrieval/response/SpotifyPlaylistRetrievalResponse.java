package mynghn.spotify.message.retrieval.response;

import mynghn.spotify.message.SpotifyPaginatedResponse;

public record SpotifyPlaylistRetrievalResponse(String description, String id, String name,
                                               SpotifyPaginatedResponse<SpotifyPlaylistItemRetrievalResponse> tracks) {

}
