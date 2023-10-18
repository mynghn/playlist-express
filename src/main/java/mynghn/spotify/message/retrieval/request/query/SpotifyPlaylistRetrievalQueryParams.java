package mynghn.spotify.message.retrieval.request.query;

import feign.Param;

public record SpotifyPlaylistRetrievalQueryParams(
        String market,
        String fields,
        @Param("additional_types") String additionalTypes) {

    public static SpotifyPlaylistRetrievalQueryParams buildDefault() {
        return new SpotifyPlaylistRetrievalQueryParams("KR", null, null);
    }
}
