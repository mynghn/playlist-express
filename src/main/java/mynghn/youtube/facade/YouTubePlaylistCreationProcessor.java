package mynghn.youtube.facade;

import feign.FeignException;
import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;
import mynghn.common.credential.CredentialManager;
import mynghn.common.ui.ConsolePrinter;
import mynghn.spotify.model.SpotifyPlaylist;
import mynghn.spotify.model.Track;
import mynghn.youtube.client.YouTubeAuthClient;
import mynghn.youtube.client.YouTubePlaylistCreationClient;
import mynghn.youtube.client.YouTubeSearchClient;
import mynghn.youtube.credential.YouTubeClientCredentials;
import mynghn.youtube.credential.YouTubeCredentialManager;
import mynghn.youtube.message.auth.response.YouTubeAuthStep1Response;
import mynghn.youtube.message.auth.response.YouTubeAuthTokenResponse;
import mynghn.youtube.message.creation.response.YouTubePlaylistCreationResponse;
import mynghn.youtube.message.search.response.YouTubeSearchResponse;
import mynghn.youtube.message.search.response.YouTubeSearchResult;
import mynghn.youtube.model.YouTubePlaylist;
import mynghn.youtube.util.YouTubePlaylistLinkBuilder;
import mynghn.youtube.util.YouTubeSearchResultEvaluator;

public class YouTubePlaylistCreationProcessor {

    private final ConsolePrinter printer;

    private final CredentialManager<YouTubeClientCredentials> credentialManager;

    private final YouTubeSearchClient searchClient;
    private final YouTubeAuthClient authClient;

    public YouTubePlaylistCreationProcessor() {
        printer = new ConsolePrinter();
        credentialManager = new YouTubeCredentialManager();

        final YouTubeClientCredentials credentials = credentialManager.getCredentials();

        searchClient = YouTubeSearchClient.connect(credentials.apiKey());
        authClient = YouTubeAuthClient.connect();
    }

    public YouTubePlaylist create(SpotifyPlaylist source) throws InterruptedException {
        // search for corresponding videos
        printer.print("Searching for YouTube videos to put in playlist...");

        final List<YouTubeSearchResult> searchResults = source.tracks().stream()
                .map(this::findCorrespondingVideo)
                .filter(Optional::isPresent) // FIXME: Handle properly w/ search failures
                .map(Optional::get)
                .toList();

        printer.print("YouTube videos search done!");

        // authorization for playlist creation
        printer.print("Authorization for YouTube playlist creation in progress...");

        final YouTubeAuthTokenResponse authResponse = doAuthorization(
                credentialManager.getCredentials());

        printer.print("YouTube API authorization done!");

        // connect creation client
        final YouTubePlaylistCreationClient creationClient = YouTubePlaylistCreationClient.connect(
                authResponse.accessToken());

        // create playlist
        printer.print("Generating a playlist copy in your YouTube account...");

        final YouTubePlaylistCreationResponse playlistCreated = creationClient.createPlaylist(
                "title",
                "description");

        // insert playlist items
        final String playlistId = playlistCreated.id();
        searchResults.forEach((searchResult) -> creationClient.addPlaylistItem(playlistId,
                "Video ID from search result" // FIXME: Replace w/ real video ID from search result
        ));

        printer.print(MessageFormat.format("YouTube playlist generation done!\n\t{0}",
                YouTubePlaylistLinkBuilder.build(playlistId)));

        return new YouTubePlaylist(); // FIXME: Build YouTube playlist object with real data
    }

    private Optional<YouTubeSearchResult> findCorrespondingVideo(Track spotifyTrack) {
        YouTubeSearchResponse searchResponse = searchClient.searchMusic(
                ""); // FIXME: Replace w/ real query
        return searchResponse.items().stream()
                .filter((searchResult) -> YouTubeSearchResultEvaluator.match(searchResult,
                        spotifyTrack))
                .findFirst();
    }

    // FIXME: Refactor to independent responsibility
    private YouTubeAuthTokenResponse doAuthorization(YouTubeClientCredentials credentials)
            throws InterruptedException {
        YouTubeAuthStep1Response step1Response = authClient.requestDeviceAndUserCodes(
                credentials.clientId());

        final String instructionMessageTemplate = """
                To process further and create a playlist in your YouTube account, permission should be granted.
                
                Please follow the steps below:
                 
                 1. Go to the following link: {0}
                 2. On the verification page, you will be prompted to enter a code.
                 3. Return to this message and enter the code provided below:
                 
                     Verification Code: {1}
                 """;

        printer.print(
                MessageFormat.format(instructionMessageTemplate,
                        step1Response.verificationUrl(), step1Response.userCode()));

        // polling
        // TODO: Implement timeout
        YouTubeAuthTokenResponse tokenResponse = null;
        do {
            try {
                tokenResponse = authClient.obtainToken(credentials.clientId(),
                        credentials.clientSecret(),
                        step1Response.deviceCode());
            } catch (FeignException e) {
                Thread.sleep(3000);
            }
        } while (tokenResponse == null);

        return tokenResponse;
    }
}
