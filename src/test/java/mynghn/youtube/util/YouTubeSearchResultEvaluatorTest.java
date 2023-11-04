package mynghn.youtube.util;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import mynghn.spotify.model.Album;
import mynghn.spotify.model.Artist;
import mynghn.spotify.model.Track;
import mynghn.youtube.message.search.response.YouTubeSearchResult;
import mynghn.youtube.message.search.response.YouTubeSearchResultSnippet;
import org.junit.jupiter.api.Test;

class YouTubeSearchResultEvaluatorTest {

    @Test
    void searchResultWithTrackNameAndOneOfArtistsNameAndAlbumNameGetsEvaluatedMatching() {
        // Arrange
        final Album testAlbum = new Album("Test album");
        final Artist testArtist1 = new Artist("Test artist 1");
        final Artist testArtist2 = new Artist("Test artist 2");
        final Track testTrack = new Track(null,
                testAlbum,
                List.of(testArtist1, testArtist2),
                null,
                0,
                null,
                "Test track",
                0
        );

        final YouTubeSearchResult testSearchResult1 = new YouTubeSearchResult(null,
                new YouTubeSearchResultSnippet(
                        String.format("This is search result title containing %s",
                                testTrack.getName()),
                        String.format("blah %s blah ~~", testAlbum.name()),
                        null,
                        "Some channel with music videos",
                        null));
        final YouTubeSearchResult testSearchResult2 = new YouTubeSearchResult(null,
                new YouTubeSearchResultSnippet(
                        String.format("This is search result title containing %s",
                                testTrack.getName()),
                        "This is an irrelevant description",
                        null,
                        String.format("%s's channel", testArtist1.name()),
                        null));
        final YouTubeSearchResult testSearchResult3 = new YouTubeSearchResult(null,
                new YouTubeSearchResultSnippet(
                        String.format("This is search result title containing %s and %s",
                                testTrack.getName(), testArtist2.name()),
                        "This is an irrelevant description",
                        null,
                        "Some channel with music videos",
                        null));
        final YouTubeSearchResult testSearchResult4 = new YouTubeSearchResult(null,
                new YouTubeSearchResultSnippet(
                        String.format("This is search result title containing %s and %s and also %s",
                                testTrack.getName(), testArtist2.name(), testAlbum.name()),
                        "This is an irrelevant description",
                        null,
                        "Some channel with music videos",
                        null));

        // Act
        final boolean evaluationResult1 = YouTubeSearchResultEvaluator.match(testSearchResult1,
                testTrack);
        final boolean evaluationResult2 = YouTubeSearchResultEvaluator.match(testSearchResult2,
                testTrack);
        final boolean evaluationResult3 = YouTubeSearchResultEvaluator.match(testSearchResult3,
                testTrack);
        final boolean evaluationResult4 = YouTubeSearchResultEvaluator.match(testSearchResult4,
                testTrack);

        // Assert
        assertTrue(evaluationResult1);
        assertTrue(evaluationResult2);
        assertTrue(evaluationResult3);
        assertTrue(evaluationResult4);
    }

    @Test
    void searchResultWithInsufficientClueGetsEvaluatedNonMatching() {
        // Arrange
        final Album testAlbum = new Album("Test album");
        final Artist testArtist1 = new Artist("Test artist 1");
        final Artist testArtist2 = new Artist("Test artist 2");
        final Track testTrack = new Track(null,
                testAlbum,
                List.of(testArtist1, testArtist2),
                null,
                0,
                null,
                "Test track",
                0
        );

        final YouTubeSearchResult testSearchResult1 = new YouTubeSearchResult(null,
                new YouTubeSearchResultSnippet(
                        String.format("This is search result title only containing %s",
                                testTrack.getName()),
                        "This is an irrelevant description",
                        null,
                        "Some channel with music videos",
                        null));
        final YouTubeSearchResult testSearchResult2 = new YouTubeSearchResult(null,
                new YouTubeSearchResultSnippet(
                        "This is search result title containing nothing",
                        String.format("blah %s blah ~~", testAlbum.name()),
                        null,
                        String.format("%s's channel", testArtist1.name()),
                        null));
        final YouTubeSearchResult testSearchResult3 = new YouTubeSearchResult(null,
                new YouTubeSearchResultSnippet(
                        String.format("This is search result title containing %s",
                                testTrack.getName()),
                        "This is an irrelevant description",
                        null,
                        String.format("%s in channel name is clueless", testAlbum.name()),
                        null));

        // Act
        final boolean evaluationResult1 = YouTubeSearchResultEvaluator.match(testSearchResult1,
                testTrack);
        final boolean evaluationResult2 = YouTubeSearchResultEvaluator.match(testSearchResult2,
                testTrack);
        final boolean evaluationResult3 = YouTubeSearchResultEvaluator.match(testSearchResult3,
                testTrack);

        // Assert
        assertFalse(evaluationResult1);
        assertFalse(evaluationResult2);
        assertFalse(evaluationResult3);
    }
}
