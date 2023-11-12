package mynghn.youtube.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;
import mynghn.spotify.model.Album;
import mynghn.spotify.model.Artist;
import mynghn.spotify.model.SpotifyTrackExternalIds;
import mynghn.spotify.model.Track;
import mynghn.youtube.client.YouTubeSearchClient;
import mynghn.youtube.message.search.response.YouTubeSearchResponse;
import mynghn.youtube.message.search.response.YouTubeSearchResult;
import mynghn.youtube.message.search.response.YouTubeSearchResultSnippet;
import mynghn.youtube.util.YouTubeSearchQueryBuilder;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class YouTubeVideoFinderTest {

    @Test
    void videoSearchMethodFallsBackToQueryWhenNoResultsFoundByRecordingCode() {
        // Arrange
        final String testRecordingCode = "International Standard Recording Code";
        final Album testAlbum = new Album("Test album");
        final Artist testArtist1 = new Artist("Test artist 1");
        final Artist testArtist2 = new Artist("Test artist 2");
        final Track testTrack = new Track(null,
                testAlbum,
                List.of(testArtist1, testArtist2),
                null,
                0,
                new SpotifyTrackExternalIds(testRecordingCode),
                "Test track",
                0
        );
        final YouTubeSearchResult matchingSearchResult = new YouTubeSearchResult(null,
                new YouTubeSearchResultSnippet(
                        String.format("This is search result title containing track name %s",
                                testTrack.getName()),
                        String.format("This is search result description containing album name %s",
                                testAlbum.name()),
                        null,
                        String.format("This is search result channel name containing one of artists' names %s",
                                testArtist1.name()),
                        null));

        final YouTubeSearchClient mockSearchClient = Mockito.mock(YouTubeSearchClient.class);
        Mockito.when(mockSearchClient.searchMusic(testRecordingCode))
                .thenReturn(new YouTubeSearchResponse(null, null, null, List.of()));
        Mockito.when(mockSearchClient.searchMusic(YouTubeSearchQueryBuilder.build(testTrack)))
                .thenReturn(new YouTubeSearchResponse(null, null, null,
                        List.of(matchingSearchResult)));

        final YouTubeVideoFinder sut = new YouTubeVideoFinder(mockSearchClient);

        // Act
        final Optional<YouTubeSearchResult> searchResultOptional = sut.findVideoMatch(testTrack);

        // Assert
        assertTrue(searchResultOptional.isPresent());
        assertEquals(matchingSearchResult, searchResultOptional.get());
    }
}
