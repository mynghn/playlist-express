package mynghn.youtube.util;

import java.util.List;
import mynghn.spotify.model.Artist;
import mynghn.spotify.model.Track;
import mynghn.youtube.message.search.response.YouTubeSearchResult;

public class YouTubeSearchResultEvaluator {

    /**
     * Checks if YouTube search result matches with given Spotify Track
     *
     * @param searchResult YouTube search result to potentially substitute Spotify Track
     * @param source       Spotify track finding its counterpart in YouTube
     * @return If the YouTube search result matches with source Spotify Track
     */
    public static boolean match(YouTubeSearchResult searchResult, Track source) {
        return containsTrackName(searchResult, source.getName()) && (
                containsArtistsName(searchResult,
                        source.getArtists().stream().map(Artist::name).toList())
                        || containsAlbumName(searchResult, source.getAlbum().name()));
    }

    private static boolean containsTrackName(YouTubeSearchResult searchResult, String trackName) {
        return searchResult.snippet().title().toLowerCase().contains(trackName.toLowerCase());
    }

    private static boolean containsArtistsName(YouTubeSearchResult searchResult,
            List<String> artistNames) {
        List<String> artistNameContainableFields = List.of(searchResult.snippet().channelTitle(),
                searchResult.snippet().title(),
                searchResult.snippet().description());

        return artistNames.stream()
                .anyMatch(artistName -> artistNameContainableFields.stream()
                        .anyMatch((searchResultField) -> searchResultField.toLowerCase()
                                .contains(artistName.toLowerCase())));
    }

    private static boolean containsAlbumName(YouTubeSearchResult searchResult, String albumName) {
        List<String> albumNameContainableFields = List.of(searchResult.snippet().title(),
                searchResult.snippet().description());

        return albumNameContainableFields.stream()
                .anyMatch((searchResultField) -> searchResultField.toLowerCase()
                        .contains(albumName.toLowerCase()));
    }
}
