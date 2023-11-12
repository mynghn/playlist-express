package mynghn.spotify.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class Track extends SpotifyPlaylistItem {

    private final Album album;

    private final List<Artist> artists;

    @SerializedName("available_markets")
    private final List<String> availableMarkets;

    @SerializedName("duration_ms")
    private final int durationMs;

    @SerializedName("external_ids")
    private final SpotifyTrackExternalIds externalIds;

    private final String name;

    @SerializedName("track_number")
    private final int trackNumber;

    @Builder
    public Track(String id,
            Album album,
            List<Artist> artists,
            List<String> availableMarkets,
            int durationMs,
            SpotifyTrackExternalIds externalIds,
            String name,
            int trackNumber) {
        super(SpotifyPlaylistItemType.track, id);

        this.album = album;
        this.artists = artists;
        this.availableMarkets = availableMarkets;
        this.durationMs = durationMs;
        this.externalIds = externalIds;
        this.name = name;
        this.trackNumber = trackNumber;
    }
}
