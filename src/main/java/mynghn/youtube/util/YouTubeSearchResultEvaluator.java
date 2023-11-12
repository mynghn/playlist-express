package mynghn.youtube.util;

import java.util.List;
import java.util.stream.Stream;
import mynghn.spotify.model.Artist;
import mynghn.spotify.model.Track;
import mynghn.youtube.message.search.response.YouTubeSearchResult;

public class YouTubeSearchResultEvaluator {

    /**
     * Strictly checks if YouTube search result matches with given Spotify Track <br/> <br/> 1.
     * Search result should contain track name in its title. <br/> <br/> Then it should either,
     * <br/> &nbsp; 2-a. Contain one of artists' names in its title, channel title or description,
     * <br/> &nbsp; 2-b. Or contain album name in its title or description.
     *
     * @param searchResult YouTube search result to potentially substitute Spotify Track
     * @param source       Spotify track finding its counterpart in YouTube
     * @return If the YouTube search result matches with source Spotify Track
     */
    public static boolean strictMatch(YouTubeSearchResult searchResult, Track source) {
        return titleContainsTrackName(searchResult, source) && (
                containsArtistsName(searchResult, source)
                        || containsAlbumName(searchResult, source));
    }

    /**
     * Leniently checks if YouTube search result matches with given Spotify Track <br/> <br/> Search
     * result should either, <br/> &nbsp; 1. Contain track name in its <i>title</i>, <br/> &nbsp; 2.
     * Or contain one of artists' names in its <i>channel title</i>, <br/> &nbsp; 3. Or contain one
     * of track name, artists' names and album name in its <i>description</i>.
     *
     * @param searchResult YouTube search result to potentially substitute Spotify Track
     * @param source       Spotify track finding its counterpart in YouTube
     * @return If the YouTube search result matches with source Spotify Track
     */
    public static boolean lenientMatch(YouTubeSearchResult searchResult, Track source) {
        return titleContainsTrackName(searchResult, source)
                || channelTitleContainsOneOfArtistsNames(searchResult, source)
                || descriptionContainsAny(searchResult, source);
    }

    private static boolean titleContainsTrackName(YouTubeSearchResult searchResult, Track source) {
        return searchResult.snippet().title().toLowerCase()
                .contains(source.getName().toLowerCase());
    }

    private static boolean containsArtistsName(YouTubeSearchResult searchResult, Track source) {
        List<String> artistNameContainableFields = List.of(searchResult.snippet().channelTitle(),
                searchResult.snippet().title(),
                searchResult.snippet().description());

        return source.getArtists().stream()
                .map(Artist::name)
                .anyMatch(artistName -> artistNameContainableFields.stream()
                        .anyMatch((searchResultField) -> searchResultField.toLowerCase()
                                .contains(artistName.toLowerCase())));
    }

    private static boolean containsAlbumName(YouTubeSearchResult searchResult, Track source) {
        List<String> albumNameContainableFields = List.of(searchResult.snippet().title(),
                searchResult.snippet().description());

        return albumNameContainableFields.stream()
                .anyMatch((searchResultField) -> searchResultField.toLowerCase()
                        .contains(source.getAlbum().name().toLowerCase()));
    }

    private static boolean channelTitleContainsOneOfArtistsNames(YouTubeSearchResult searchResult,
            Track source) {
        return source.getArtists().stream()
                .map(Artist::name)
                .map(String::toLowerCase)
                .anyMatch(artistNameLowered -> searchResult.snippet().channelTitle().toLowerCase()
                        .contains(artistNameLowered));
    }

    private static boolean descriptionContainsAny(YouTubeSearchResult searchResult, Track source) {
        return Stream.concat(Stream.of(source.getName(), source.getAlbum().name()),
                        source.getArtists().stream().map(Artist::name))
                .map(String::toLowerCase)
                .anyMatch(fieldLowered -> searchResult.snippet().description().toLowerCase()
                        .contains(fieldLowered));
    }
}
