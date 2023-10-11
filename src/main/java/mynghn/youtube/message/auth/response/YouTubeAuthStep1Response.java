package mynghn.youtube.message.auth.response;

public record YouTubeAuthStep1Response(String deviceCode,
                                       int expiresIn, int interval,
                                       String userCode,
                                       String verificationUrl) {


}
