package mynghn.spotify.credential;

import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;
import lombok.RequiredArgsConstructor;
import mynghn.common.config.AppConfigKey;
import mynghn.common.config.AppConfigs;
import mynghn.common.exception.FileReadException;

@RequiredArgsConstructor
public class SpotifyCredentialsJsonFileReader {

    private final AppConfigs configs;

    public SpotifyClientCredentials read() {
        try (BufferedReader reader = new BufferedReader(new FileReader(getFileName()))) {
            return new Gson().fromJson(reader, SpotifyClientCredentials.class);
        } catch (FileNotFoundException e) {
            return null;
        } catch (IOException e) {
            throw new FileReadException(
                    "IOException occurred while reading Spotify client credentials file.", e);
        }
    }

    public Optional<SpotifyClientCredentials> readOptional() {
        SpotifyClientCredentials credentialsRead = read();
        if (credentialsRead == null) {
            return Optional.empty();
        }
        return Optional.of(credentialsRead);
    }

    public SpotifyClientCredentials orElseRead(
            Supplier<SpotifyClientCredentials> credentialsSupplier) {
        return readOptional().orElseGet(credentialsSupplier);
    }

    private String getFileName() {
        return Objects.requireNonNull(getClass().getResource(
                configs.get(AppConfigKey.SPOTIFY_CREDENTIAL_PATH.toString()))).getFile();
    }
}
