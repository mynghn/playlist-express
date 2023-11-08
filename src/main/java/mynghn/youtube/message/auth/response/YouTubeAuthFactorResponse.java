package mynghn.youtube.message.auth.response;

import com.google.gson.annotations.SerializedName;

public record YouTubeAuthFactorResponse(@SerializedName("device_code") String deviceCode,
                                        @SerializedName("user_code") String userCode,
                                        @SerializedName("verification_url") String verificationUrl,
                                        @SerializedName("expires_in") int expiresIn,
                                        int interval) {

}
