package mynghn.spotify.credential;

import lombok.RequiredArgsConstructor;
import mynghn.common.util.EnvVarReader;

@RequiredArgsConstructor
public class SpotifyCredentialsEnvVarReader {

    private final String clientIdVarName;
    private final String clientSecretVarName;

    public SpotifyClientCredentials read() {
        return new SpotifyClientCredentials(EnvVarReader.read(clientIdVarName),
                EnvVarReader.read(clientSecretVarName));
    }
}
