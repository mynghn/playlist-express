package mynghn.spotify.facade;

import mynghn.common.config.AppConfigKey;
import mynghn.common.config.AppConfigs;
import mynghn.common.credential.CredentialManager;
import mynghn.common.ui.ConsolePrinter;
import mynghn.common.util.FileUtils;
import mynghn.spotify.client.SpotifyAuthClient;
import mynghn.spotify.client.SpotifyPlaylistRetrievalClient;
import mynghn.spotify.credential.LocalSpotifyCredentialsReader;
import mynghn.spotify.credential.SpotifyClientCredentials;
import mynghn.spotify.credential.SpotifyCredentialsEnvVarReader;
import mynghn.spotify.credential.SpotifyCredentialsJsonFileReader;
import mynghn.spotify.message.auth.response.auth.SpotifyAuthResponse;
import mynghn.spotify.message.retrieval.response.SpotifyPlaylistRetrievalResponse;
import mynghn.spotify.model.SpotifyPlaylist;
import mynghn.spotify.util.PlaylistConverter;
import mynghn.spotify.util.SpotifyLinkParser;

public class SpotifyPlaylistRetrievalProcessor {

    private static final String CLIENT_ID_ENV_VAR_NAME = "SPOTIFY_CLIENT_ID";
    private static final String CLIENT_SECRET_ENV_VAR_NAME = "SPOTIFY_CLIENT_SECRET";

    private final ConsolePrinter printer;

    private final CredentialManager<SpotifyClientCredentials> credentialManager;

    public SpotifyPlaylistRetrievalProcessor(AppConfigs configs) {
        printer = new ConsolePrinter();

        credentialManager = buildCredentialManager(configs);
    }

    private static CredentialManager<SpotifyClientCredentials> buildCredentialManager(
            AppConfigs configs) {
        return new LocalSpotifyCredentialsReader(
                new SpotifyCredentialsJsonFileReader(FileUtils.getResourceFullPath(
                        configs.get(AppConfigKey.SPOTIFY_CREDENTIAL_PATH))),
                new SpotifyCredentialsEnvVarReader(CLIENT_ID_ENV_VAR_NAME,
                        CLIENT_SECRET_ENV_VAR_NAME));
    }

    private static SpotifyAuthClient buildAuthClient() {
        return SpotifyAuthClient.connect();
    }

    private static SpotifyPlaylistRetrievalClient buildRetrievalClient(String accessToken) {
        return SpotifyPlaylistRetrievalClient.connect(accessToken);
    }

    public SpotifyPlaylist fetch(String link) {
        // 0. Auth phase
        printer.print("Spotify API authorization in progress...");

        final SpotifyAuthClient authClient = buildAuthClient();

        final SpotifyAuthResponse authResponse = authClient.obtainToken(
                credentialManager.getCredentials());

        printer.print("Spotify API authorization done!");

        // Fetch playlist info
        printer.print("Fetching Spotify playlist info via API...");

        final SpotifyPlaylistRetrievalClient retrievalClient = buildRetrievalClient(
                authResponse.accessToken());

        final String playlistId = SpotifyLinkParser.extractPlaylistId(link);

        final SpotifyPlaylistRetrievalResponse retrievalResponse = retrievalClient.fetch(
                playlistId);

        printer.print("Spotify playlist retrieval done!");

        return PlaylistConverter.fromResponse(retrievalResponse);
    }
}
