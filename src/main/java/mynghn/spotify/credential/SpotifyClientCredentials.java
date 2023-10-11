package mynghn.spotify.credential;

/**
 * Data object of registered Spotify App's client credentials
 * @param clientId Client ID of a registered Spotify App
 * @param clientSecret Client Secret of a registered Spotify App
 */
public record SpotifyClientCredentials(String clientId, String clientSecret) {

}
