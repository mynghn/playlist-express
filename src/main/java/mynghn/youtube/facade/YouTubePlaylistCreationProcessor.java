package mynghn.youtube.facade;

import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;
import mynghn.common.config.AppConfigKey;
import mynghn.common.config.AppConfigs;
import mynghn.common.credential.CredentialManager;
import mynghn.common.ui.ConsolePrinter;
import mynghn.common.util.FileUtils;
import mynghn.spotify.model.SpotifyPlaylist;
import mynghn.spotify.model.Track;
import mynghn.youtube.client.YouTubeAuthClient;
import mynghn.youtube.client.YouTubePlaylistCreationClient;
import mynghn.youtube.client.YouTubeSearchClient;
import mynghn.youtube.credential.LocalYouTubeCredentialsLazyReader;
import mynghn.youtube.credential.YouTubeClientCredentials;
import mynghn.youtube.credential.YouTubeCredentialsEnvVarReader;
import mynghn.youtube.credential.YouTubeCredentialsJsonFileReader;
import mynghn.youtube.message.YouTubeResourceId;
import mynghn.youtube.message.YouTubeVideoId;
import mynghn.youtube.message.auth.response.YouTubeAuthFactorResponse;
import mynghn.youtube.message.auth.response.YouTubeAuthTokenResponse;
import mynghn.youtube.message.creation.response.YouTubePlaylistItemResourceResponse;
import mynghn.youtube.message.creation.response.YouTubePlaylistResourceResponse;
import mynghn.youtube.message.search.response.YouTubeSearchResult;
import mynghn.youtube.model.YouTubePlaylist;
import mynghn.youtube.service.YouTubeAuthPollingAgent;
import mynghn.youtube.service.YouTubeVideoFinder;
import mynghn.youtube.util.PlaylistConverter;
import mynghn.youtube.util.YouTubePlaylistLinkBuilder;

public class YouTubePlaylistCreationProcessor {

    private static final String API_KEY_ENV_VAR_NAME = "YOUTUBE_API_KEY";
    private static final String CLIENT_ID_ENV_VAR_NAME = "YOUTUBE_CLIENT_ID";
    private static final String CLIENT_SECRET_ENV_VAR_NAME = "YOUTUBE_CLIENT_SECRET";

    private final ConsolePrinter printer;
    private final CredentialManager<YouTubeClientCredentials> credentialManager;
    private final YouTubeAuthClient authClient;
    private final YouTubeVideoFinder videoFinder;

    public YouTubePlaylistCreationProcessor(AppConfigs configs) {
        printer = new ConsolePrinter();

        credentialManager = buildCredentialManager(configs);

        videoFinder = new YouTubeVideoFinder(
                YouTubeSearchClient.connect(credentialManager.getCredentials().apiKey()));

        authClient = YouTubeAuthClient.connect();
    }

    private static CredentialManager<YouTubeClientCredentials> buildCredentialManager(
            AppConfigs configs) {
        return new LocalYouTubeCredentialsLazyReader(
                new YouTubeCredentialsJsonFileReader(FileUtils.getResourceFullPath(
                        configs.get(AppConfigKey.YOUTUBE_CREDENTIAL_PATH))),
                new YouTubeCredentialsEnvVarReader(
                        API_KEY_ENV_VAR_NAME,
                        CLIENT_ID_ENV_VAR_NAME,
                        CLIENT_SECRET_ENV_VAR_NAME));
    }

    private static YouTubePlaylistItemResourceResponse addSearchResultToPlaylist(
            YouTubePlaylistCreationClient creationClient,
            YouTubeSearchResult searchResult,
            String playlistId) {
        YouTubeResourceId searchResultId = searchResult.id();
        if (!(searchResultId instanceof YouTubeVideoId)) {
            throw new IllegalArgumentException("YouTube search result is not a video.");
        }
        return creationClient.addPlaylistItem(playlistId,
                ((YouTubeVideoId) searchResultId).getVideoId());
    }

    public YouTubePlaylist create(SpotifyPlaylist source) {
        // search for corresponding videos
        printer.print("Searching for YouTube videos match tracks in source playlist...");

        final List<YouTubeSearchResult> searchResults = source.tracks().stream()
                .map(this::searchTrackAndNotifyIfNotFound)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();

        printer.print("YouTube videos search done!");

        // authorization for playlist creation
        final YouTubeAuthTokenResponse authTokenResponse = doAuthorization();

        // connect creation client
        final YouTubePlaylistCreationClient creationClient = YouTubePlaylistCreationClient.connect(
                authTokenResponse.accessToken());

        // create playlist
        printer.print("Generating a playlist copy in your YouTube account...");

        final YouTubePlaylistResourceResponse emptyPlaylist = creationClient.createPlaylist(
                source.name(),
                source.description());

        // insert playlist items
        List<YouTubePlaylistItemResourceResponse> playlistItemsAdded = searchResults.stream()
                .map(searchResult -> addSearchResultToPlaylist(creationClient,
                        searchResult,
                        emptyPlaylist.id()))
                .toList();

        final YouTubePlaylist youtubePlaylist = PlaylistConverter.fromResponse(emptyPlaylist,
                playlistItemsAdded);

        printer.print(MessageFormat.format("YouTube playlist created!\n\t{0}",
                YouTubePlaylistLinkBuilder.build(youtubePlaylist)));

        return youtubePlaylist;
    }

    private Optional<YouTubeSearchResult> searchTrackAndNotifyIfNotFound(Track spotifyTrack) {
        Optional<YouTubeSearchResult> searchResultOptional = videoFinder.findVideoMatch(
                spotifyTrack);
        if (searchResultOptional.isEmpty()) {
            printer.print(
                    MessageFormat.format("Failed to find YouTube video for {0}", spotifyTrack));
        }
        return searchResultOptional;
    }

    private YouTubeAuthTokenResponse doAuthorization() {
        printer.print("Authorization for YouTube playlist creation in progress...");

        YouTubeClientCredentials credentials = credentialManager.getCredentials();

        // Request device code & user code first
        YouTubeAuthFactorResponse authFactorResponse = authClient.requestDeviceAndUserCodes(
                credentials.clientId());

        // Display instructions for the next step user verification
        displayUserVerificationInstructions(authFactorResponse.verificationUrl(),
                authFactorResponse.userCode());

        YouTubeAuthPollingAgent authPollingAgent = YouTubeAuthPollingAgent.builder()
                .authClient(authClient)
                .clientId(credentials.clientId())
                .clientSecret(credentials.clientSecret())
                .deviceCode(authFactorResponse.deviceCode())
                .build();

        YouTubeAuthTokenResponse authTokenResponse = authPollingAgent.poll();

        printer.print("YouTube API authorization done!");

        return authTokenResponse;
    }

    private void displayUserVerificationInstructions(String verificationUrl, String userCode) {
        final String instructionMessageTemplate = """
                To process further and create a playlist in your YouTube account,
                                
                Please follow the steps below and grant permissions to this app:
                 
                 1. Go to the following link:
                 
                    {0}
                    
                 2. On the verification page, you will be prompted to enter a code.
                 
                 3. Return to this message and enter the code provided below:
                 
                     Verification Code: {1}
                 """;

        printer.print(MessageFormat.format(instructionMessageTemplate, verificationUrl, userCode));
    }
}
