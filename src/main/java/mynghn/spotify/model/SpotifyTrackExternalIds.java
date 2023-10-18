package mynghn.spotify.model;

import com.google.gson.annotations.SerializedName;

/**
 * IDs for external use, of a Spotify Track
 *
 * @param intlStdRecCode <a href="https://en.wikipedia.org/wiki/International_Article_Number">International Standard Recording Code</a>
 */
public record SpotifyTrackExternalIds(@SerializedName("isrc") String intlStdRecCode) {

}
