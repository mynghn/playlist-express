package mynghn.youtube.enums;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;

@Getter
public enum PrivacyStatus {

    @SerializedName("private") PRIVATE("private"),
    @SerializedName("public") PUBLIC("public"),
    @SerializedName("unlisted") UNLISTED("unlisted");

    private final String value;

    PrivacyStatus(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
