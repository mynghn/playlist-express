package mynghn.spotify.client;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import feign.Feign;
import feign.Headers;
import feign.Param;
import feign.QueryMap;
import feign.Request;
import feign.RequestLine;
import feign.gson.DoubleToIntMapTypeAdapter;
import feign.gson.GsonDecoder;
import java.util.Map;
import mynghn.common.auth.BearerAuthRequestInterceptor;
import mynghn.spotify.deserializer.SpotifyPlaylistItemDeserializer;
import mynghn.spotify.enums.BaseUrl;
import mynghn.spotify.enums.EndPointTemplates;
import mynghn.spotify.message.retrieval.request.query.SpotifyPlaylistRetrievalQueryParams;
import mynghn.spotify.message.retrieval.response.SpotifyPlaylistRetrievalResponse;
import mynghn.spotify.model.SpotifyPlaylistItem;

public interface SpotifyPlaylistRetrievalClient {

    /**
     * Build Feign client with Spotify access token in Bearer authentication header.
     *
     * @param accessToken Spotify access token obtained through authorization request
     * @return Properly configured SpotifyPlaylistRetrievalClient Feign client instance
     */
    static SpotifyPlaylistRetrievalClient connect(String accessToken) {
        return Feign.builder()
                // TODO: Add error decoder
                .requestInterceptor(new BearerAuthRequestInterceptor(accessToken))
                .decoder(new GsonDecoder(new GsonBuilder()
                        .setPrettyPrinting()
                        .registerTypeAdapter(new TypeToken<Map<String, Object>>() {}.getType(),
                                new DoubleToIntMapTypeAdapter())
                        .registerTypeAdapter(SpotifyPlaylistItem.class,
                                new SpotifyPlaylistItemDeserializer())
                        .create()))
                .options(new Request.Options())
                .target(SpotifyPlaylistRetrievalClient.class, BaseUrl.WEB_API_V1.getValue());
    }

    /**
     * Retrieve Spotify playlist information
     *
     * @param id     Spotify ID of the playlist
     * @param params Request query parameters
     * @return API response with playlist information
     */
    @RequestLine(EndPointTemplates.RETRIEVE_PLAYLIST)
    @Headers("Accept-Language: ko-KR,ko") // TODO: Support other languages
    SpotifyPlaylistRetrievalResponse fetch(@Param("id") String id,
            @QueryMap SpotifyPlaylistRetrievalQueryParams params);

    /**
     * Retrieve Spotify playlist information (w/ default query parameter options)
     *
     * @param id Spotify ID of the playlist
     * @return API response with playlist information
     */
    default SpotifyPlaylistRetrievalResponse fetch(String id) {
        return fetch(id, SpotifyPlaylistRetrievalQueryParams.buildDefault());
    }
}
