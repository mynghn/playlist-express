package mynghn.youtube.service;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import mynghn.spotify.model.Track;
import mynghn.youtube.client.YouTubeSearchClient;
import mynghn.youtube.message.search.response.YouTubeSearchResponse;
import mynghn.youtube.message.search.response.YouTubeSearchResult;
import mynghn.youtube.util.YouTubeSearchQueryBuilder;
import mynghn.youtube.util.YouTubeSearchResultEvaluator;

@RequiredArgsConstructor
public class YouTubeVideoFinder {

    private final YouTubeSearchClient searchClient;

    /**
     * Finds YouTube video counterpart of a Spotify track
     *
     * @param spotifyTrack Spotify track from a playlist
     * @return Optional search result object from YouTube. Empty if no matching result is found.
     */
    public Optional<YouTubeSearchResult> findVideoMatch(Track spotifyTrack) {
        Optional<YouTubeSearchResult> searchResultOptional = Optional.empty();
        if (spotifyTrack.getExternalIds().intlStdRecCode() != null) {
            searchResultOptional = findByCode(spotifyTrack);
        }

        if (searchResultOptional.isEmpty()) {
            searchResultOptional = findByQuery(spotifyTrack);
        }

        return searchResultOptional;
    }

    private Optional<YouTubeSearchResult> findByCode(Track spotifyTrack) {
        YouTubeSearchResponse searchResponse = searchClient.searchMusic(
                spotifyTrack.getExternalIds().intlStdRecCode());

        return searchResponse.items().stream()
                .filter(searchResult -> YouTubeSearchResultEvaluator.lenientMatch(searchResult,
                        spotifyTrack))
                .findFirst();
    }

    private Optional<YouTubeSearchResult> findByQuery(Track spotifyTrack) {
        YouTubeSearchResponse searchResponse = searchClient.searchMusic(
                YouTubeSearchQueryBuilder.build(spotifyTrack));

        return searchResponse.items().stream()
                .filter((searchResult) -> YouTubeSearchResultEvaluator.strictMatch(searchResult,
                        spotifyTrack))
                .findFirst();
    }
}
