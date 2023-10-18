package mynghn.spotify.credential;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import java.util.Optional;
import mynghn.common.config.AppConfigKey;
import mynghn.common.config.AppConfigs;
import org.junit.jupiter.api.Test;

class LocalSpotifyCredentialReaderTest {

    @Test
    void spotifyCredentialsLoadedFromFileAndProvided() {
        /* Arrange */
        final String testCredentialsPath = "/credential/test_credentials.json";
        final String testClientId = "Test ID";
        final String testClientSecret = "Test Secret";

        // Stub AppConfigs
        AppConfigs mockAppConfigs = mock(AppConfigs.class);
        when(mockAppConfigs.get(AppConfigKey.SPOTIFY_CREDENTIAL_PATH.toString())).thenReturn(
                testCredentialsPath);

        final LocalSpotifyCredentialReader sut = new LocalSpotifyCredentialReader(
                new SpotifyCredentialsJsonFileReader(mockAppConfigs),
                mock(SpotifyCredentialsEnvVarReader.class));

        /* Act */
        SpotifyClientCredentials credentialsFromSUT = sut.getCredentials();

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

        // Stub SpotifyCredentialsJsonFileReader
        SpotifyCredentialsJsonFileReader spyFileReader = spy(
                new SpotifyCredentialsJsonFileReader(null));
        doReturn(Optional.empty()).when(spyFileReader).readOptional();

        // Stub SpotifyCredentialsEnvVarReader
        SpotifyCredentialsEnvVarReader mockEnvVarReader = mock(
                SpotifyCredentialsEnvVarReader.class);
        when(mockEnvVarReader.read()).thenReturn(testCredentials);

        LocalSpotifyCredentialReader sut = new LocalSpotifyCredentialReader(spyFileReader,
                mockEnvVarReader);

        /* Act */
        SpotifyClientCredentials credentialsFromSUT = sut.getCredentials();

        /* Assert */
        assertEquals(testCredentials, credentialsFromSUT);
    }
}
