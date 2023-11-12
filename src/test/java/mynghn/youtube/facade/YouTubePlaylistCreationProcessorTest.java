package mynghn.youtube.facade;

import static org.junit.jupiter.api.Assertions.assertEquals;

import feign.FeignException;
import java.util.List;
import mynghn.common.credential.CredentialManager;
import mynghn.common.ui.ConsolePrinter;
import mynghn.spotify.model.Album;
import mynghn.spotify.model.Artist;
import mynghn.spotify.model.SpotifyPlaylist;
import mynghn.spotify.model.SpotifyTrackExternalIds;
import mynghn.spotify.model.Track;
import mynghn.youtube.client.YouTubeAuthClient;
import mynghn.youtube.client.YouTubePlaylistCreationClient;
import mynghn.youtube.client.YouTubeSearchClient;
import mynghn.youtube.credential.LocalYouTubeCredentialsLazyReader;
import mynghn.youtube.credential.YouTubeClientCredentials;
import mynghn.youtube.enums.AccessScopes;
import mynghn.youtube.enums.PrivacyStatus;
import mynghn.youtube.enums.YouTubeResourceKind;
import mynghn.youtube.message.YouTubeResourceId;
import mynghn.youtube.message.YouTubeVideoId;
import mynghn.youtube.message.auth.response.YouTubeAuthFactorResponse;
import mynghn.youtube.message.auth.response.YouTubeAuthTokenResponse;
import mynghn.youtube.message.creation.response.YouTubePlaylistItemResourceResponse;
import mynghn.youtube.message.creation.response.YouTubePlaylistItemResourceResponse.PlaylistItemContentDetails;
import mynghn.youtube.message.creation.response.YouTubePlaylistItemResourceResponse.PlaylistItemSnippet;
import mynghn.youtube.message.creation.response.YouTubePlaylistItemResourceResponse.PlaylistItemStatus;
import mynghn.youtube.message.creation.response.YouTubePlaylistResourceResponse;
import mynghn.youtube.message.creation.response.YouTubePlaylistResourceResponse.PlaylistContentDetails;
import mynghn.youtube.message.creation.response.YouTubePlaylistResourceResponse.PlaylistSnippet;
import mynghn.youtube.message.creation.response.YouTubePlaylistResourceResponse.PlaylistStatus;
import mynghn.youtube.message.search.response.YouTubeSearchResponse;
import mynghn.youtube.message.search.response.YouTubeSearchResult;
import mynghn.youtube.message.search.response.YouTubeSearchResultSnippet;
import mynghn.youtube.model.YouTubePlaylist;
import mynghn.youtube.service.YouTubeVideoFinder;
import mynghn.youtube.util.PlaylistConverter;
import mynghn.youtube.util.YouTubeSearchQueryBuilder;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

class YouTubePlaylistCreationProcessorTest {


    @Test
    void playlistCreationProcessSucceeds() {
        /* Arrange */
        final SpotifyPlaylist sourcePlaylist = new SpotifyPlaylist(
                "This is description text of test playlist.",
                "Test playlist ID",
                "Test Playlist",
                List.of(Track.builder()
                                .name("Test track #1")
                                .artists(List.of(new Artist("Test artist 1")))
                                .album(new Album("Test album 1"))
                                .externalIds(new SpotifyTrackExternalIds(null))
                                .build(),
                        Track.builder()
                                .name("Test track #2")
                                .artists(List.of(new Artist("Test artist 1"),
                                        new Artist("Test artist 2")))
                                .album(new Album("Test album 2"))
                                .externalIds(new SpotifyTrackExternalIds(null))
                                .build(),
                        Track.builder()
                                .name("Test track #3")
                                .artists(List.of(new Artist("Test artist 2")))
                                .album(new Album("Test album 3"))
                                .externalIds(new SpotifyTrackExternalIds(
                                        "Recording code for Test track #3"))
                                .build()));

        // Mock console printer
        final ConsolePrinter mockPrinter = Mockito.mock(ConsolePrinter.class);

        // Stub credential manager
        final CredentialManager<YouTubeClientCredentials> mockCredentialManager = Mockito.mock(
                LocalYouTubeCredentialsLazyReader.class);
        final YouTubeClientCredentials testCredentials = new YouTubeClientCredentials(
                "Test API Key",
                "Test Client ID",
                "Test Client Secret");
        Mockito.when(mockCredentialManager.getCredentials()).thenReturn(testCredentials);

        // Stub auth client
        final YouTubeAuthClient mockAuthClient = Mockito.mock(YouTubeAuthClient.class);
        // 1st step
        final YouTubeAuthFactorResponse testAuthFactorResponse = new YouTubeAuthFactorResponse(
                "Test Device Code",
                "Test User Code",
                "Test Verification URL",
                0, 0);
        Mockito.when(mockAuthClient.requestDeviceAndUserCodes(testCredentials.clientId()))
                .thenReturn(testAuthFactorResponse);
        // 2nd step
        final var pollCounter = new Object() {
            int count = 0;
        };
        final FeignException dummyFeignException = Mockito.mock(FeignException.class);
        final YouTubeAuthTokenResponse testAuthTokenResponse = new YouTubeAuthTokenResponse(
                "test-access-token",
                "Bearer",
                AccessScopes.MANAGE_ACCOUNT,
                0,
                "test-refresh-token");
        Mockito.when(mockAuthClient.obtainToken(testCredentials.clientId(),
                        testCredentials.clientSecret(),
                        testAuthFactorResponse.deviceCode()))
                .then(invocation -> {
                    pollCounter.count++;
                    if (pollCounter.count < 5) {
                        throw dummyFeignException;
                    }
                    return testAuthTokenResponse;
                });

        // Stub search client
        final YouTubeSearchClient mockSearchClient = Mockito.mock(YouTubeSearchClient.class);
        final Track sourceTrack1 = sourcePlaylist.tracks().get(0);
        final YouTubeSearchResult validSearchResult1 = new YouTubeSearchResult(
                new YouTubeVideoId("Test Video ID 1"),
                new YouTubeSearchResultSnippet(
                        String.format("Search result title with track name %s",
                                sourceTrack1.getName()),
                        "Random description",
                        null,
                        String.format("Channel title with artist name %s",
                                sourceTrack1.getArtists().get(0).name()),
                        null));
        Mockito.when(mockSearchClient.searchMusic(YouTubeSearchQueryBuilder.build(sourceTrack1)))
                .thenReturn(new YouTubeSearchResponse(null,
                        null,
                        null,
                        List.of(validSearchResult1)));
        final Track sourceTrack2 = sourcePlaylist.tracks().get(1);
        final YouTubeSearchResult invalidSearchResult2 = new YouTubeSearchResult(
                new YouTubeResourceId(YouTubeResourceKind.CHANNEL.getValue()) {
                },
                new YouTubeSearchResultSnippet(
                        String.format("Search result title with track name %s",
                                sourceTrack2.getName()),
                        "Random description",
                        null,
                        "Random channel title",
                        null));
        Mockito.when(mockSearchClient.searchMusic(YouTubeSearchQueryBuilder.build(sourceTrack2)))
                .thenReturn(new YouTubeSearchResponse(null,
                        null,
                        null,
                        List.of(invalidSearchResult2)));
        final Track sourceTrack3 = sourcePlaylist.tracks().get(2);
        final YouTubeSearchResult validSearchResult3 = new YouTubeSearchResult(
                new YouTubeVideoId("Test Video ID 3"),
                new YouTubeSearchResultSnippet(
                        "Random title",
                        String.format("Description with artist name %s",
                                sourceTrack3.getArtists().get(0).name()),
                        null,
                        "Random channel title",
                        null));
        Mockito.when(mockSearchClient.searchMusic(YouTubeSearchQueryBuilder.build(sourceTrack3)))
                .thenReturn(new YouTubeSearchResponse(null,
                        null,
                        null,
                        List.of()));
        Mockito.when(mockSearchClient.searchMusic(sourceTrack3.getExternalIds().intlStdRecCode()))
                .thenReturn(new YouTubeSearchResponse(null,
                        null,
                        null,
                        List.of(validSearchResult3)));

        // Stub playlist creation client
        final YouTubePlaylistCreationClient mockCreationClient = Mockito.mock(
                YouTubePlaylistCreationClient.class);
        // create empty playlist
        final YouTubePlaylistResourceResponse testPlaylistCreationResponse = new YouTubePlaylistResourceResponse(
                null,
                new PlaylistSnippet(null,
                        sourcePlaylist.name(),
                        sourcePlaylist.description(),
                        null,
                        null),
                new PlaylistStatus(PrivacyStatus.PRIVATE),
                new PlaylistContentDetails(0));
        Mockito.when(mockCreationClient.createPlaylist(sourcePlaylist.name(),
                        sourcePlaylist.description()))
                .thenReturn(testPlaylistCreationResponse);
        // add playlist items
        final YouTubePlaylistItemResourceResponse testPlaylistItemInsertionResponse1 = new YouTubePlaylistItemResourceResponse(
                null,
                new PlaylistItemSnippet(null,
                        validSearchResult1.snippet().title(),
                        validSearchResult1.snippet().description(),
                        validSearchResult1.snippet().channelId(),
                        validSearchResult1.snippet().channelTitle(),
                        "",
                        "",
                        testPlaylistCreationResponse.id(),
                        0,
                        validSearchResult1.id()),
                new PlaylistItemStatus(PrivacyStatus.PRIVATE),
                new PlaylistItemContentDetails(
                        ((YouTubeVideoId) validSearchResult1.id()).getVideoId(),
                        null));
        Mockito.when(mockCreationClient.addVideoToPlaylist(testPlaylistCreationResponse.id(),
                        ((YouTubeVideoId) validSearchResult1.id()).getVideoId()))
                .thenReturn(testPlaylistItemInsertionResponse1);
        final YouTubePlaylistItemResourceResponse testPlaylistItemInsertionResponse2 = new YouTubePlaylistItemResourceResponse(
                null,
                new PlaylistItemSnippet(null,
                        validSearchResult3.snippet().title(),
                        validSearchResult3.snippet().description(),
                        validSearchResult3.snippet().channelId(),
                        validSearchResult3.snippet().channelTitle(),
                        "",
                        "",
                        testPlaylistCreationResponse.id(),
                        0,
                        validSearchResult3.id()),
                new PlaylistItemStatus(PrivacyStatus.PRIVATE),
                new PlaylistItemContentDetails(
                        ((YouTubeVideoId) validSearchResult3.id()).getVideoId(),
                        null));
        Mockito.when(mockCreationClient.addVideoToPlaylist(testPlaylistCreationResponse.id(),
                        ((YouTubeVideoId) validSearchResult3.id()).getVideoId()))
                .thenReturn(testPlaylistItemInsertionResponse2);

        /* Arrange & Act & Assert */
        try (final MockedStatic<YouTubePlaylistCreationClient> mockedStaticCreationClient = Mockito.mockStatic(
                YouTubePlaylistCreationClient.class)) {
            mockedStaticCreationClient.when(() -> YouTubePlaylistCreationClient.connect(
                            testAuthTokenResponse.accessToken()))
                    .thenReturn(mockCreationClient);

            final YouTubePlaylistCreationProcessor sut = new YouTubePlaylistCreationProcessor(
                    mockPrinter,
                    mockCredentialManager,
                    mockAuthClient,
                    new YouTubeVideoFinder(mockSearchClient));

            // Act
            final YouTubePlaylist youTubePlaylistActual = sut.create(sourcePlaylist);

            // Assert
            final YouTubePlaylist youTubePlaylistExpected = PlaylistConverter.fromResponse(
                    testPlaylistCreationResponse,
                    List.of(testPlaylistItemInsertionResponse1,
                            testPlaylistItemInsertionResponse2));

            assertEquals(youTubePlaylistExpected.id(), youTubePlaylistActual.id());
            assertEquals(youTubePlaylistExpected.title(), youTubePlaylistActual.title());
            assertEquals(youTubePlaylistExpected.description(), youTubePlaylistActual.description());
            assertEquals(youTubePlaylistExpected.channel(), youTubePlaylistActual.channel());
            assertEquals(youTubePlaylistExpected.privacyStatus(), youTubePlaylistActual.privacyStatus());
            assertEquals(youTubePlaylistExpected.items(), youTubePlaylistActual.items());
        }
    }
}
