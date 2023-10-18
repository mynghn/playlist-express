package mynghn.spotify.model;

import com.google.gson.annotations.SerializedName;

public enum SpotifyPlaylistItemType {

    @SerializedName("track") track,
    @SerializedName("episode") episode,
}
