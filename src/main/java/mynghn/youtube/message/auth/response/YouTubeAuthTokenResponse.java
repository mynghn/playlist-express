package mynghn.youtube.message.auth.response;

import com.google.gson.annotations.SerializedName;

public record YouTubeAuthTokenResponse(@SerializedName("access_token") String accessToken,
                                       @SerializedName("token_type") String tokenType,
                                       String scope,
                                       @SerializedName("expires_in") int expiresIn,
                                       @SerializedName("refresh_token") String refreshToken) {

}
