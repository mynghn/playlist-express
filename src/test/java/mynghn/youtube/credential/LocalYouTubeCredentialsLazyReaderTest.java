package mynghn.youtube.credential;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Objects;
import java.util.Optional;
import mynghn.common.util.JsonFileReader;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

class LocalYouTubeCredentialsLazyReaderTest {

    @Test
    void credentialsLoadedFromFile() {
        /* Arrange */
        final String testCredentialsPath = Objects.requireNonNull(
                getClass().getResource("/credential/youtube/test_credentials.json")).getFile();
        final String testApiKey = "Test API Key";
        final String testClientId = "Test ID";
        final String testClientSecret = "Test Secret";

        final LocalYouTubeCredentialsLazyReader sut = new LocalYouTubeCredentialsLazyReader(
                new YouTubeCredentialsJsonFileReader(testCredentialsPath),
                mock(YouTubeCredentialsEnvVarReader.class));

        /* Act */
        final YouTubeClientCredentials credentialsFromSUT = sut.getCredentials();

        /* Assert */
        assertEquals(testApiKey, credentialsFromSUT.apiKey());
        assertEquals(testClientId, credentialsFromSUT.clientId());
        assertEquals(testClientSecret, credentialsFromSUT.clientSecret());
    }

    @Test
    void credentialsLoadedFromEnvVarsIfNoFileFound() {
        /* Arrange */
        final String testApiKey = "Test API Key";
        final String testClientId = "Test ID";
        final String testClientSecret = "Test Secret";
        final YouTubeClientCredentials testCredentials = new YouTubeClientCredentials(testApiKey,
                testClientId,
                testClientSecret);
        final String invalidFilePath = "No File";

        // Stub YouTubeCredentialsJsonFileReader
        final YouTubeCredentialsJsonFileReader spyFileReader = spy(
                new YouTubeCredentialsJsonFileReader(invalidFilePath));

        // Stub YouTubeCredentialsEnvVarReader
        final YouTubeCredentialsEnvVarReader mockEnvVarReader = mock(
                YouTubeCredentialsEnvVarReader.class);
        when(mockEnvVarReader.read()).thenReturn(testCredentials);

        final LocalYouTubeCredentialsLazyReader sut = new LocalYouTubeCredentialsLazyReader(
                spyFileReader,
                mockEnvVarReader);

        /* Act & Assert within JsonFileReader static mocked context */
        try (final MockedStatic<JsonFileReader> mockJsonFileReader = mockStatic(
                JsonFileReader.class)) {
            mockJsonFileReader.when(() -> JsonFileReader.readIfFound(invalidFilePath,
                    YouTubeClientCredentials.class)).thenReturn(Optional.empty());

            // Act
            YouTubeClientCredentials credentialsFromSUT = sut.getCredentials();

            // Assert
            assertEquals(testCredentials, credentialsFromSUT);
        }
    }

    @Test
    void credentialsLoadedFromCacheAfterFirstCall() {
        /* Arrange */
        final String testCredentialsPath = Objects.requireNonNull(
                getClass().getResource("/credential/youtube/test_credentials.json")).getFile();

        final YouTubeCredentialsJsonFileReader spyFileReader = spy(
                new YouTubeCredentialsJsonFileReader(testCredentialsPath));
        final LocalYouTubeCredentialsLazyReader sut = new LocalYouTubeCredentialsLazyReader(
                spyFileReader,
                mock(YouTubeCredentialsEnvVarReader.class));

        /* Act & Assert */
        final YouTubeClientCredentials credentialsFromFile = sut.getCredentials();
        verify(spyFileReader, times(1)).orElseRead(any());
        final YouTubeClientCredentials credentialsCached = sut.getCredentials();
        verify(spyFileReader, times(1)).orElseRead(any());

        assertEquals(credentialsFromFile, credentialsCached);
    }
}
