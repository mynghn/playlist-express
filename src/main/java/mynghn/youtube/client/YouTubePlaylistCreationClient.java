package mynghn.youtube.client;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import feign.Body;
import feign.Feign;
import feign.Headers;
import feign.Param;
import feign.Request;
import feign.RequestLine;
import feign.gson.DoubleToIntMapTypeAdapter;
import feign.gson.GsonDecoder;
import java.util.Map;
import mynghn.common.auth.BearerAuthRequestInterceptor;
import mynghn.youtube.deserializer.YouTubeResourceIdDeserializer;
import mynghn.youtube.enums.BaseUrl;
import mynghn.youtube.enums.EndPointTemplates;
import mynghn.youtube.enums.PrivacyStatus;
import mynghn.youtube.message.YouTubeResourceId;
import mynghn.youtube.message.creation.response.YouTubePlaylistItemResourceResponse;
import mynghn.youtube.message.creation.response.YouTubePlaylistResourceResponse;

public interface YouTubePlaylistCreationClient {

    PrivacyStatus DEFAULT_PLAYLIST_PRIVACY_STATUS = PrivacyStatus.PRIVATE;

    /**
     * Build Feign client with OAuth 2.0 access token in Bearer authentication header.
     *
     * @param accessToken OAuth 2.0 Google API access token
     * @return Properly configured YouTubePlaylistCreationClient Feign client instance
     */
    static YouTubePlaylistCreationClient connect(String accessToken) {
        return Feign.builder()
                // TODO: Add error decoder
                .decoder(new GsonDecoder(new GsonBuilder()
                        .setPrettyPrinting()
                        .registerTypeAdapter(new TypeToken<Map<String, Object>>() {
                                }.getType(),
                                new DoubleToIntMapTypeAdapter())
                        .registerTypeAdapter(YouTubeResourceId.class,
                                new YouTubeResourceIdDeserializer())
                        .create()))
                .requestInterceptor(new BearerAuthRequestInterceptor(accessToken))
                .options(new Request.Options())
                .target(YouTubePlaylistCreationClient.class,
                        BaseUrl.YOUTUBE_DATA_API_V3.getValue());
    }

    /**
     * Create empty YouTube Playlist.
     *
     * @param title         Playlist title
     * @param description   Playlist description
     * @param privacyStatus Playlist privacy status.
     * @return API response representing created YouTube playlist resource
     */
    @RequestLine(EndPointTemplates.PLAYLISTS_INSERT + "?part=snippet,status")
    @Headers("Content-Type: application/json")
    @Body("%7B\"snippet\": %7B\"title\": \"{title}\", \"description\": \"{description}\"%7D, \"status\":%7B\"privacyStatus\": \"{privacyStatus}\"%7D%7D")
    YouTubePlaylistResourceResponse createPlaylist(@Param("title") String title,
            @Param("description") String description,
            @Param("privacyStatus") PrivacyStatus privacyStatus);

    /**
     * Create empty YouTube playlist with default playlist privacy status.
     *
     * @param title       Playlist title
     * @param description Playlist description
     * @return API response representing created YouTube playlist resource
     */
    default YouTubePlaylistResourceResponse createPlaylist(String title, String description) {
        return createPlaylist(title, description, DEFAULT_PLAYLIST_PRIVACY_STATUS);
    }

    /**
     * Append a playlist item(video) to YouTube playlist as the last item.
     *
     * @param playlistId YouTube Playlist ID to append item to
     * @param videoId    Video ID of the playlist item being inserted
     * @return API response representing appended YouTube playlist item resource
     */
    @RequestLine(EndPointTemplates.PLAYLIST_ITEMS_INSERT + "?part=snippet")
    @Headers("Content-Type: application/json")
    @Body("%7B\"snippet\": %7B\"playlistId\": \"{playlistId}\", \"resourceId\":%7B\"kind\": \"youtube#video\", \"videoId\": \"{videoId}\"%7D%7D%7D")
    YouTubePlaylistItemResourceResponse addPlaylistItem(@Param("playlistId") String playlistId,
            @Param("videoId") String videoId);

    /**
     * Insert a playlist item(video) to YouTube playlist's given position.
     *
     * @param playlistId YouTube Playlist ID to insert item into
     * @param videoId    Video ID of the playlist item being inserted
     * @param position   Order of the playlist item in the playlist after insertion
     * @return API response representing inserted YouTube playlist item resource
     */
    @RequestLine(EndPointTemplates.PLAYLIST_ITEMS_INSERT + "?part=snippet")
    @Headers("Content-Type: application/json")
    @Body("%7B\"snippet\": %7B\"playlistId\": \"{playlistId}\", \"resourceId\":%7B\"kind\": \"youtube#video\", \"videoId\": \"{videoId}\"%7D, \"position\": {position}%7D%7D")
    YouTubePlaylistItemResourceResponse insertPlaylistItem(@Param("playlistId") String playlistId,
            @Param("videoId") String videoId,
            @Param("position") int position);
}
