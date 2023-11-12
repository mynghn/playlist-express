package mynghn.youtube.credential;

import com.google.gson.annotations.SerializedName;

/**
 * Data object containing registered Google Cloud project's client credentials
 *
 * @param apiKey       API key of a registered Google Cloud project
 * @param clientId     OAuth 2.0 Client ID of a registered Google Cloud project
 * @param clientSecret OAuth 2.0 Client Secret of a registered Google Cloud project
 */
public record YouTubeClientCredentials(@SerializedName("api_key") String apiKey,
                                       @SerializedName("client_id") String clientId,
                                       @SerializedName("client_secret") String clientSecret) {

}
