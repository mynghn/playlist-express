package mynghn.spotify.credential;

import mynghn.common.credential.CredentialManager;

public class SpotifyCredentialManager implements CredentialManager<SpotifyClientCredentials> {

    @Override
    public SpotifyClientCredentials getCredentials() {
        // TODO: Implement lazy load strategy from a file or env variables
        return null;
    }
}
