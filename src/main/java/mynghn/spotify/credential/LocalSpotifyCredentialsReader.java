package mynghn.spotify.credential;

import lombok.RequiredArgsConstructor;
import mynghn.common.credential.CredentialManager;

@RequiredArgsConstructor
public class LocalSpotifyCredentialsReader implements CredentialManager<SpotifyClientCredentials> {

    private final SpotifyCredentialsJsonFileReader fileReader;
    private final SpotifyCredentialsEnvVarReader envVarReader;

    @Override
    public SpotifyClientCredentials getCredentials() {
        return fileReader.orElseRead(envVarReader::read);
    }
}
