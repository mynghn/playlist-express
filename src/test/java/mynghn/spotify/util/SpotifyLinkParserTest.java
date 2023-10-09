package mynghn.spotify.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;

import org.junit.jupiter.api.Test;

class SpotifyLinkParserTest {

    @Test
    void playlistIdExtractedFromOnlyBaseUrlAndId() {
        // Arrange
        final String testLink = "https://open.spotify.com/playlist/0ABHnDQzpFP8i79utY9TB6";
        final String testId = "0ABHnDQzpFP8i79utY9TB6";

        // Act
        String actualId = SpotifyLinkParser.extractPlaylistId(testLink);

        // Assert
        assertEquals(testId, actualId);
    }

    @Test
    void playlistIdExtractedFromLinkWithAdditionalQueryStrings() {
        // Arrange
        final String testId = "0Xn5R3vLNvNrBXIRSBgrwr";
        final String testLink = String.format(
                "https://open.spotify.com/playlist/%s?si=fbf387ab8db3484e&param2=content&param3=&param4=1,2",
                testId);

        // Act
        String actualExtractedId = SpotifyLinkParser.extractPlaylistId(testLink);

        // Assert
        assertEquals(testId, actualExtractedId);
    }

    @Test
    void illegalArgumentExceptionThrownWhenInvalidLinkEncountered() {
        // Arrange
        final String linkWithInvalidBaseUrl = "https://close.spotify.com/playlist/0Xn5R3vLNvNrBXIRSBgrwr";
        final String linkWithInvalidPlaylistId = "https://open.spotify.com/playlist/non-base62-symbol+=";
        final String linkWithInvalidQueryString = "https://open.spotify.com/playlist/0Xn5R3vLNvNrBXIRSBgrwr?invalidquerystring=?";

        // Act & Assert
        assertThrowsExactly(IllegalArgumentException.class,
                () -> SpotifyLinkParser.extractPlaylistId(linkWithInvalidBaseUrl));
        assertThrowsExactly(IllegalArgumentException.class,
                () -> SpotifyLinkParser.extractPlaylistId(linkWithInvalidPlaylistId));
        assertThrowsExactly(IllegalArgumentException.class,
                () -> SpotifyLinkParser.extractPlaylistId(linkWithInvalidQueryString));
    }
}
