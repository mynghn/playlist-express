package mynghn.youtube.credential;

import java.util.function.Supplier;
import lombok.RequiredArgsConstructor;
import mynghn.common.util.JsonFileReader;

@RequiredArgsConstructor
public class YouTubeCredentialsJsonFileReader {

    private static final Class<YouTubeClientCredentials> credentialsClass = YouTubeClientCredentials.class;

    private final String filePath;

    public YouTubeClientCredentials orElseRead(
            Supplier<YouTubeClientCredentials> credentialsSupplier) {
        return JsonFileReader.readIfFound(filePath, credentialsClass)
                .orElseGet(credentialsSupplier);
    }
}
