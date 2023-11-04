package mynghn.youtube.util;

import java.text.MessageFormat;
import java.util.List;
import java.util.stream.Collectors;
import mynghn.spotify.model.Artist;
import mynghn.spotify.model.Track;

public class YouTubeSearchQueryBuilder {

    /**
     * Builds a search query to find a YouTube video match of Spotify track
     *
     * @param spotifyTrack Spotify track from a playlist
     * @return Search query containing track, artists and album info
     */
    public static String build(Track spotifyTrack) {
        return MessageFormat.format("{0} by {1} from {2} album",
                spotifyTrack.getName(),
                concatArtistNames(spotifyTrack.getArtists()),
                spotifyTrack.getAlbum().name());
    }

    private static String concatArtistNames(List<Artist> spotifyArtists) {
        return spotifyArtists.stream()
                .map(Artist::name)
                .collect(Collectors.joining(", "));
    }
}
