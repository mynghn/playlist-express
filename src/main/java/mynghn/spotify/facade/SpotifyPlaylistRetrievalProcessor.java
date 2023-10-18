package mynghn.spotify.facade;

import mynghn.common.config.AppConfigs;
import mynghn.common.credential.CredentialManager;
import mynghn.common.ui.ConsolePrinter;
import mynghn.spotify.client.SpotifyAuthClient;
import mynghn.spotify.client.SpotifyPlaylistRetrievalClient;
import mynghn.spotify.credential.LocalSpotifyCredentialReader;
import mynghn.spotify.credential.SpotifyClientCredentials;
import mynghn.spotify.credential.SpotifyCredentialsEnvVarReader;
import mynghn.spotify.credential.SpotifyCredentialsJsonFileReader;
import mynghn.spotify.message.auth.response.auth.SpotifyAuthResponse;
import mynghn.spotify.message.retrieval.response.SpotifyPlaylistRetrievalResponse;
import mynghn.spotify.model.SpotifyPlaylist;
import mynghn.spotify.util.PlaylistConverter;
import mynghn.spotify.util.SpotifyLinkParser;

public class SpotifyPlaylistRetrievalProcessor {

    private final ConsolePrinter printer;

    private final CredentialManager<SpotifyClientCredentials> credentialManager;

    public SpotifyPlaylistRetrievalProcessor() {
        printer = new ConsolePrinter();
        credentialManager = new LocalSpotifyCredentialReader(
                new SpotifyCredentialsJsonFileReader(new AppConfigs()),
                new SpotifyCredentialsEnvVarReader());
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
