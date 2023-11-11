package mynghn.youtube.util;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import mynghn.spotify.model.Track;
import mynghn.youtube.client.YouTubeSearchClient;
import mynghn.youtube.message.search.response.YouTubeSearchResponse;
import mynghn.youtube.message.search.response.YouTubeSearchResult;

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
        Optional<YouTubeSearchResult> searchResultOptional = findByQuery(spotifyTrack);

        if (searchResultOptional.isEmpty()
                && spotifyTrack.getExternalIds().intlStdRecCode() != null) {
            searchResultOptional = findByCode(spotifyTrack);
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
                .filter((searchResult) -> YouTubeSearchResultEvaluator.match(searchResult,
                        spotifyTrack))
                .findFirst();
    }
}
