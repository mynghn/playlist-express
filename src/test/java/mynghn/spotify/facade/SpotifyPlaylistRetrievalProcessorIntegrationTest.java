package mynghn.spotify.facade;

import mynghn.spotify.model.SpotifyPlaylist;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class SpotifyPlaylistRetrievalProcessorIntegrationTest {
    SpotifyPlaylistRetrievalProcessor sut = new SpotifyPlaylistRetrievalProcessor();

    @Test
    void spotifyPlaylistFetchedOk() {
        // Arrange
        final String testSpotifyPlaylistId = "0oEiFXi46Od0bOl6QEmXdB";
        final String testSpotifyPlaylistLink = String.format(
                "https://open.spotify.com/playlist/%s?si=e088f069657c442d",
                testSpotifyPlaylistId);

        SpotifyPlaylist playlistFetched = sut.fetch(testSpotifyPlaylistLink);

        // Assert
        Assertions.assertEquals(testSpotifyPlaylistId, playlistFetched.id());
        Assertions.assertNotEquals(0, playlistFetched.tracks().size());

        playlistFetched.tracks().forEach(track -> Assertions.assertNotNull(track.getId()));
    }
}
