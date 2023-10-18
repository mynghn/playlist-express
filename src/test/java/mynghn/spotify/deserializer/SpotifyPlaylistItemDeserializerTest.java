package mynghn.spotify.deserializer;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import mynghn.spotify.model.Track;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class SpotifyPlaylistItemDeserializerTest {

    private final SpotifyPlaylistItemDeserializer sut = new SpotifyPlaylistItemDeserializer();

    private static JsonObject buildTrackJsonTestData() {
        JsonObject album = new JsonObject();
        album.addProperty("name", "Test Album");

        JsonObject artist1 = new JsonObject();
        album.addProperty("name", "Test Artist 1");
        JsonObject artist2 = new JsonObject();
        album.addProperty("name", "Test Artist 2");
        JsonArray artists = new JsonArray();
        artists.add(artist1);
        artists.add(artist2);

        JsonObject track = new JsonObject();
        track.addProperty("type", "track");
        track.addProperty("name", "Test Track");
        track.addProperty("id", "Test Track's Spotify ID");
        track.add("album", album);
        track.add("artists", artists);

        return track;
    }

    private static JsonObject buildEpisodeJsonTestData() {
        JsonObject episode = new JsonObject();
        episode.addProperty("type", "episode");
        episode.addProperty("name", "Test Episode");
        episode.addProperty("id", "Test Episode's Spotify ID");

        return episode;
    }

    @Test
    void trackJsonObjProperlyDelegatedToGsonContext() {
        // Arrange
        JsonObject trackJson = buildTrackJsonTestData();
        JsonDeserializationContext mockGsonCtx = mock(JsonDeserializationContext.class);

        // Act
        sut.deserialize(trackJson, null, mockGsonCtx);

        // Assert
        verify(mockGsonCtx).deserialize(trackJson, Track.class);
    }

    @Test
    void unsupportedOperationExceptionThrownWhenEpisodeJsonObjEncountered() {
        // Arrange
        JsonObject episodeJson = buildEpisodeJsonTestData();

        // Act && Assert
        Assertions.assertThrowsExactly(UnsupportedOperationException.class,
                () -> sut.deserialize(episodeJson, null, null));
    }
}
