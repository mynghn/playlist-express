package mynghn.spotify.credential;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import java.util.Objects;
import java.util.Optional;
import mynghn.common.util.JsonFileReader;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

class LocalSpotifyCredentialReaderTest {

    @Test
    void spotifyCredentialsLoadedFromFileAndProvided() {
        /* Arrange */
        final String testCredentialsPath = Objects.requireNonNull(
                getClass().getResource("/credential/test_credentials.json")).getFile();
        final String testClientId = "Test ID";
        final String testClientSecret = "Test Secret";

        final LocalSpotifyCredentialReader sut = new LocalSpotifyCredentialReader(
                new SpotifyCredentialsJsonFileReader(testCredentialsPath),
                mock(SpotifyCredentialsEnvVarReader.class));

        /* Act */
        final SpotifyClientCredentials credentialsFromSUT = sut.getCredentials();

        /* Assert */
        assertEquals(testClientId, credentialsFromSUT.clientId());
        assertEquals(testClientSecret, credentialsFromSUT.clientSecret());
    }

    @Test
    void spotifyCredentialsLoadedFromEnvVarsAndProvided() {
        /* Arrange */
        final String testClientId = "Test ID";
        final String testClientSecret = "Test Secret";
        final SpotifyClientCredentials testCredentials = new SpotifyClientCredentials(testClientId,
                testClientSecret);
        final String invalidFilePath = "No File";

        // Stub SpotifyCredentialsJsonFileReader
        final SpotifyCredentialsJsonFileReader spyFileReader = spy(
                new SpotifyCredentialsJsonFileReader(invalidFilePath));

        // Stub SpotifyCredentialsEnvVarReader
        final SpotifyCredentialsEnvVarReader mockEnvVarReader = mock(
                SpotifyCredentialsEnvVarReader.class);
        when(mockEnvVarReader.read()).thenReturn(testCredentials);

        final LocalSpotifyCredentialReader sut = new LocalSpotifyCredentialReader(spyFileReader,
                mockEnvVarReader);

        /* Act & Assert within JsonFileReader static mocked context */
        try (MockedStatic<JsonFileReader> mockJsonFileReader = mockStatic(JsonFileReader.class)) {
            mockJsonFileReader.when(() -> JsonFileReader.readIfFound(invalidFilePath,
                    SpotifyClientCredentials.class)).thenReturn(Optional.empty());

            // Act
            SpotifyClientCredentials credentialsFromSUT = sut.getCredentials();

            // Assert
            assertEquals(testCredentials, credentialsFromSUT);
        }
    }
}
