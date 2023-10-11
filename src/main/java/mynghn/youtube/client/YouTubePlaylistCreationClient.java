package mynghn.youtube.client;

import feign.Body;
import feign.Feign;
import feign.Headers;
import feign.Param;
import feign.Request;
import feign.RequestLine;
import mynghn.common.auth.BearerAuthRequestInterceptor;
import mynghn.youtube.enums.BaseUrl;
import mynghn.youtube.enums.EndPointTemplates;
import mynghn.youtube.enums.PlaylistPrivacyStatus;
import mynghn.youtube.message.creation.response.YouTubePlaylistCreationResponse;
import mynghn.youtube.message.creation.response.YouTubePlaylistItemInsertionResponse;

public interface YouTubePlaylistCreationClient {

    PlaylistPrivacyStatus DEFAULT_PLAYLIST_PRIVACY_STATUS = PlaylistPrivacyStatus.PRIVATE;

    /**
     * Build Feign client with OAuth 2.0 access token in Bearer authentication header.
     *
     * @param accessToken OAuth 2.0 Google API access token
     * @return Properly configured YouTubePlaylistCreationClient Feign client instance
     */
    static YouTubePlaylistCreationClient connect(String accessToken) {
        return Feign.builder()
                // TODO: Add response decoder
                // TODO: Add error decoder
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
    YouTubePlaylistCreationResponse createPlaylist(@Param String title, @Param String description,
            @Param PlaylistPrivacyStatus privacyStatus);

    /**
     * Create empty YouTube playlist with default playlist privacy status.
     *
     * @param title       Playlist title
     * @param description Playlist description
     * @return API response representing created YouTube playlist resource
     */
    default YouTubePlaylistCreationResponse createPlaylist(String title, String description) {
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
    YouTubePlaylistItemInsertionResponse addPlaylistItem(@Param String playlistId,
            @Param String videoId);

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
    YouTubePlaylistItemInsertionResponse insertPlaylistItem(@Param String playlistId,
            @Param String videoId, @Param int position);
}
