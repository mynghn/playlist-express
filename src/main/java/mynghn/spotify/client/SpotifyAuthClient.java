package mynghn.spotify.client;

import feign.Body;
import feign.Feign;
import feign.Headers;
import feign.Param;
import feign.Request;
import feign.RequestLine;
import mynghn.common.auth.BasicAuthHeaderBuilder;
import mynghn.spotify.credential.SpotifyClientCredentials;
import mynghn.spotify.enums.BaseUrl;
import mynghn.spotify.enums.EndPointTemplates;
import mynghn.spotify.message.auth.response.auth.SpotifyAuthResponse;


public interface SpotifyAuthClient {

    /**
     * Build Feign client for Spotify API authorization request.
     *
     * @return Properly configured SpotifyAuthClient Feign client instance
     */
    static SpotifyAuthClient connect() {
        return Feign.builder()
                // TODO: Add response decoder
                // TODO: Add error decoder
                .options(new Request.Options()) // with default options
                .target(SpotifyAuthClient.class, BaseUrl.AUTH.getValue());
    }

    /**
     * Obtain Spotify Web API access token through <a
     * href="https://developer.spotify.com/documentation/web-api/tutorials/client-credentials-flow">
     * Client Credentials flow
     * </a>.
     *
     * @param basicAuthHeader Authorization header value for HTTP Basic authentication built with
     *                        client credentials
     * @return API response with access token
     */
    @RequestLine(EndPointTemplates.OBTAIN_ACCESS_TOKEN)
    @Headers({"Content-Type: application/x-www-form-urlencoded",
            "Authorization: {basicAuthHeader}"})
    @Body("grant_type=client_credentials")
    SpotifyAuthResponse obtainTokenThroughClientCredentialsFlow(@Param String basicAuthHeader);

    /**
     * Obtain Spotify Web API access token through <a
     * href="https://developer.spotify.com/documentation/web-api/tutorials/client-credentials-flow">
     * Client Credentials flow
     * </a>.
     *
     * @param credentials Spotify app's client credentials
     * @return API response with access token
     */
    default SpotifyAuthResponse obtainToken(SpotifyClientCredentials credentials) {
        return obtainTokenThroughClientCredentialsFlow(
                BasicAuthHeaderBuilder.build(credentials.clientId(), credentials.clientSecret()));
    }
}
