package mynghn.youtube.credential;

import mynghn.common.credential.CredentialManager;

public class YouTubeCredentialManager implements CredentialManager<YouTubeClientCredentials> {

    @Override
    public YouTubeClientCredentials getCredentials() {
        // TODO: Implement lazy load strategy from a file or env variables
        return null;
    }
}
