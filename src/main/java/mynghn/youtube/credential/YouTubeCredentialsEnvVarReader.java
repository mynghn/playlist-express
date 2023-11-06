package mynghn.youtube.credential;

import lombok.RequiredArgsConstructor;
import mynghn.common.util.EnvVarReader;

@RequiredArgsConstructor
public class YouTubeCredentialsEnvVarReader {

    private final String apiKeyVarName;
    private final String clientIdVarName;
    private final String clientSecretVarName;

    public YouTubeClientCredentials read() {
        return new YouTubeClientCredentials(EnvVarReader.read(apiKeyVarName),
                EnvVarReader.read(clientIdVarName),
                EnvVarReader.read(clientSecretVarName));
    }
}
