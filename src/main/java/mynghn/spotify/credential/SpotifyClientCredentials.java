package mynghn.spotify.credential;

import com.google.gson.annotations.SerializedName;

/**
 * Data object of registered Spotify App's client credentials
 *
 * @param clientId     Client ID of a registered Spotify App
 * @param clientSecret Client Secret of a registered Spotify App
 */
public record SpotifyClientCredentials(@SerializedName("client_id") String clientId,
                                       @SerializedName("client_secret") String clientSecret) {

}
