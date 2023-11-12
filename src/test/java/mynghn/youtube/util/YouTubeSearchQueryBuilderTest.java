package mynghn.youtube.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import mynghn.spotify.model.Album;
import mynghn.spotify.model.Artist;
import mynghn.spotify.model.Track;
import org.junit.jupiter.api.Test;

class YouTubeSearchQueryBuilderTest {

    @Test
    void searchQueryProperlyConstructedWithTrackNameArtistNamesAndAlbumName() {
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
        final String queryExpected = String.format("%s by %s, %s from %s album",
                testTrack.getName(), testArtist1.name(), testArtist2.name(), testAlbum.name());

        // Act
        final String queryActual = YouTubeSearchQueryBuilder.build(testTrack);

        // Assert
        assertEquals(queryExpected, queryActual);
    }
}
