package mynghn.spotify.credential;

import java.util.function.Supplier;
import lombok.RequiredArgsConstructor;
import mynghn.common.util.JsonFileReader;

@RequiredArgsConstructor
public class SpotifyCredentialsJsonFileReader {

    private static final Class<SpotifyClientCredentials> credentialsClass = SpotifyClientCredentials.class;

    private final String filePath;

    public SpotifyClientCredentials orElseRead(
            Supplier<SpotifyClientCredentials> credentialsSupplier) {
        return JsonFileReader.readIfFound(filePath, credentialsClass)
                .orElseGet(credentialsSupplier);
    }
}
