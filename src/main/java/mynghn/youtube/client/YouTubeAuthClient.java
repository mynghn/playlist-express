package mynghn.youtube.client;

import feign.Body;
import feign.Feign;
import feign.Headers;
import feign.Param;
import feign.Request;
import feign.RequestLine;
import feign.gson.GsonDecoder;
import mynghn.youtube.enums.AccessScopes;
import mynghn.youtube.enums.BaseUrl;
import mynghn.youtube.enums.EndPointTemplates;
import mynghn.youtube.message.auth.response.YouTubeAuthFactorResponse;
import mynghn.youtube.message.auth.response.YouTubeAuthTokenResponse;

public interface YouTubeAuthClient {

    /**
     * Build Feign client for authorization.
     *
     * @return Properly configured YouTubeAuthClient Feign client instance
     */
    static YouTubeAuthClient connect() {
        return Feign.builder()
                // TODO: Add error decoder
                .decoder(new GsonDecoder())
                .options(new Request.Options())
                .target(YouTubeAuthClient.class, BaseUrl.OAUTH2.getValue());
    }

    /**
     * Request device code, user code and verification url for following authorization step on a
     * separate device.
     *
     * @param clientId OAuth 2.0 Client ID of a registered Google Cloud project
     * @return API response containing device code, user code and verification url
     */
    @RequestLine(EndPointTemplates.REQUEST_AUTH_DEVICE_N_USER_CODE)
    @Headers("Content-Type: application/x-www-form-urlencoded")
    @Body("client_id={clientId}" + "&scope=" + AccessScopes.MANAGE_ACCOUNT)
    YouTubeAuthFactorResponse requestDeviceAndUserCodes(@Param("clientId") String clientId);

    /**
     * Request OAuth 2.0 token from server. <br/> Call this method to poll Google authorization
     * server. Success response with tokens will be returned when user completes 2nd step
     * verification on a separate device.
     *
     * @param clientId     OAuth 2.0 Client ID of a registered Google Cloud project
     * @param clientSecret OAuth 2.0 Client Secret of a registered Google Cloud project
     * @param deviceCode   Device code acquired from the
     *                     {@link YouTubeAuthClient#requestDeviceAndUserCodes 1st step}
     * @return Success response with access token, refresh token and other related information
     */
    @RequestLine(EndPointTemplates.OBTAIN_TOKEN)
    @Headers("Content-Type: application/x-www-form-urlencoded")
    @Body("client_id={clientId}&client_secret={clientSecret}&device_code={deviceCode}"
            + "&grant_type=urn:ietf:params:oauth:grant-type:device_code")
    YouTubeAuthTokenResponse obtainToken(@Param("clientId") String clientId,
            @Param("clientSecret") String clientSecret,
            @Param("deviceCode") String deviceCode);
}
