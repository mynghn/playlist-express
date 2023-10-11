package mynghn.youtube.credential;

/**
 * Data object containing registered Google Cloud project's client credentials
 *
 * @param apiKey       API key of a registered Google Cloud project
 * @param clientId     OAuth 2.0 Client ID of a registered Google Cloud project
 * @param clientSecret OAuth 2.0 Client Secret of a registered Google Cloud project
 */
public record YouTubeClientCredentials(String apiKey, String clientId, String clientSecret) {

}
