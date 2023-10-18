package mynghn.spotify.credential;

import mynghn.common.credential.EnvVarDoesNotExistException;

public class SpotifyCredentialsEnvVarReader {

    private static final String CLIENT_ID_ENV_VAR_NAME = "SPOTIFY_CLIENT_ID";
    private static final String CLIENT_SECRET_ENV_VAR_NAME = "SPOTIFY_CLIENT_SECRET";

    public SpotifyClientCredentials read() {
        return read(CLIENT_ID_ENV_VAR_NAME, CLIENT_SECRET_ENV_VAR_NAME);
    }

    public SpotifyClientCredentials read(String clientIdEnvVarName, String clientSecretEnvVarName) {
        String clientId = System.getenv(clientIdEnvVarName);
        if (clientId == null) {
            throw EnvVarDoesNotExistException.of(clientIdEnvVarName);
        }

        String clientSecret = System.getenv(clientSecretEnvVarName);
        if (clientSecret == null) {
            throw EnvVarDoesNotExistException.of(clientSecretEnvVarName);
        }

        return new SpotifyClientCredentials(clientId, clientSecret);
    }
}
