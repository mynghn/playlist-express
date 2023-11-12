package mynghn.youtube.credential;

import lombok.RequiredArgsConstructor;
import mynghn.common.credential.CredentialManager;

@RequiredArgsConstructor
public class LocalYouTubeCredentialsLazyReader implements
        CredentialManager<YouTubeClientCredentials> {

    private final YouTubeCredentialsJsonFileReader fileReader;
    private final YouTubeCredentialsEnvVarReader envVarReader;
    private YouTubeClientCredentials credentials = null;

    /**
     * Lazy loads YouTube API credentials. <br/> On load, tries reading from file first and then
     * falls back to environment variables if file read fails.
     *
     * @return YouTube API credentials obj
     */
    @Override
    public YouTubeClientCredentials getCredentials() {
        if (credentials == null) {
            credentials = fileReader.orElseRead(envVarReader::read);
        }
        return credentials;
    }
}
