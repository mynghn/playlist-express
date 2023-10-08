package mynghn.spotify.client;

import feign.Feign;
import feign.Param;
import feign.QueryMap;
import feign.Request;
import feign.RequestLine;
import mynghn.common.auth.BearerAuthRequestInterceptor;
import mynghn.spotify.enums.BaseUrl;
import mynghn.spotify.enums.EndPointTemplates;
import mynghn.spotify.message.retrieval.request.query.SpotifyPlaylistRetrievalQueryParams;
import mynghn.spotify.message.retrieval.response.SpotifyPlaylistRetrievalResponse;

public interface SpotifyPlaylistRetrievalClient {

    /**
     * Build Feign client with Spotify access token in Bearer authentication header.
     *
     * @param accessToken Spotify access token obtained through authorization request
     * @return Properly configured SpotifyPlaylistRetrievalClient Feign client instance
     */
    static SpotifyPlaylistRetrievalClient connect(String accessToken) {
        return Feign.builder()
                // TODO: Add response decoder
                // TODO: Add error decoder
                .requestInterceptor(new BearerAuthRequestInterceptor(accessToken))
                .options(new Request.Options())
                .target(SpotifyPlaylistRetrievalClient.class, BaseUrl.WEB_API_V1.getValue());
    }

    /**
     * Retrieve Spotify playlist information
     *
     * @param id      Spotify ID of the playlist
     * @param queries Request query parameters
     * @return API response with playlist information
     */
    @RequestLine(EndPointTemplates.RETRIEVE_PLAYLIST)
    SpotifyPlaylistRetrievalResponse fetch(@Param String id,
            @QueryMap SpotifyPlaylistRetrievalQueryParams queries);

    /**
     * Retrieve Spotify playlist information w/ default query parameter options
     *
     * @param id      Spotify ID of the playlist
     * @return API response with playlist information
     */
    default SpotifyPlaylistRetrievalResponse fetch(String id) {
        return fetch(id, SpotifyPlaylistRetrievalQueryParams.empty());
    }
}
