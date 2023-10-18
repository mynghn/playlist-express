package mynghn.spotify.message.auth.response.auth;

import com.google.gson.annotations.SerializedName;

public record SpotifyAuthResponse(@SerializedName("access_token") String accessToken,
                                  @SerializedName("token_type") String tokenType,
                                  @SerializedName("expires_in") int expiresIn) {

}
