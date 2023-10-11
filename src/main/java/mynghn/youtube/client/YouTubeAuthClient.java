package mynghn.youtube.client;

import feign.Body;
import feign.Feign;
import feign.Headers;
import feign.Param;
import feign.Request;
import feign.RequestLine;
import mynghn.youtube.enums.AccessScopes;
import mynghn.youtube.enums.BaseUrl;
import mynghn.youtube.enums.EndPointTemplates;
import mynghn.youtube.message.auth.response.YouTubeAuthStep1Response;
import mynghn.youtube.message.auth.response.YouTubeAuthTokenResponse;

public interface YouTubeAuthClient {

    /**
     * Build Feign client for authorization.
     *
     * @return Properly configured YouTubeAuthClient Feign client instance
     */
    static YouTubeAuthClient connect() {
        return Feign.builder()
                // TODO: Add response decoder
                // TODO: Add error decoder
                .options(new Request.Options())
                .target(YouTubeAuthClient.class, BaseUrl.OAUTH2.getValue());
    }

    /**
     * Request device code, user code and verification url for following 2-factor verification
     *
     * @param clientId OAuth 2.0 Client ID of a registered Google Cloud project
     * @return API response with device code, user code and verification url
     */
    @RequestLine(EndPointTemplates.REQUEST_AUTH_DEVICE_N_USER_CODE)
    @Headers("Content-Type: application/x-www-form-urlencoded")
    @Body("client_id={clientId}" + "&scope=" + AccessScopes.MANAGE_ACCOUNT)
    YouTubeAuthStep1Response requestDeviceAndUserCodes(@Param String clientId);

    /**
     * Request OAuth 2.0 token from server. Call this method to poll Google authorization server.
     * Success response with token is returned if the user completed 2-factor verification.
     *
     * @param clientId     OAuth 2.0 Client ID of a registered Google Cloud project
     * @param clientSecret OAuth 2.0 Client Secret of a registered Google Cloud project
     * @param deviceCode   Device code previously acquired from
     *                     {@link YouTubeAuthClient#requestDeviceAndUserCodes first step auth
     *                     request}
     * @return Success response w/ access token, refresh token and related information
     */
    @RequestLine(EndPointTemplates.OBTAIN_TOKEN)
    @Headers("Content-Type: application/x-www-form-urlencoded")
    @Body("client_id={clientId}&client_secret={clientSecret}&device_code={deviceCode}"
            + "&grant_type=urn:ietf:params:oauth:grant-type:device_code")
    YouTubeAuthTokenResponse obtainToken(@Param String clientId, @Param String clientSecret,
            @Param String deviceCode);
}
