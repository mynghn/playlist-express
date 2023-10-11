package mynghn.youtube.message.auth.response;

public record YouTubeAuthTokenResponse(String accessToken,
                                       int expiresIn,
                                       String refreshToken,
                                       String scope,
                                       String tokenType) {


}
