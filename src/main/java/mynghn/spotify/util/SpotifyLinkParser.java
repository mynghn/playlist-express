package mynghn.spotify.util;

import java.text.MessageFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SpotifyLinkParser {

    private static final String PLAYLIST_BASE_URL = "https:\\/\\/open\\.spotify\\.com\\/playlist";
    private static final String PLAYLIST_ID_CAPTURING_GROUP_NAME = "playlistId";
    private static final Pattern PLAYLIST_LINK_PATTERN = buildPlaylistLinkPattern(PLAYLIST_BASE_URL,
            PLAYLIST_ID_CAPTURING_GROUP_NAME);

    private static Pattern buildPlaylistLinkPattern(String baseUrl, String idCapturingGroupName) {
        final String base62SymbolPattern = "[A-Za-z0-9]";
        final String playlistIdCapturingGroup = MessageFormat.format("(?<{0}>{1}+)",
                idCapturingGroupName, base62SymbolPattern);

        final String additionalQueryStringPattern = "(\\?[^?=]+\\=[^?=]*(\\&[^?=]+\\=[^?=]*)*)?";

        return Pattern.compile(
                MessageFormat.format("^{0}\\/{1}{2}$",
                        baseUrl, playlistIdCapturingGroup, additionalQueryStringPattern));
    }

    public static String extractPlaylistId(String link) {
        Matcher matcher = PLAYLIST_LINK_PATTERN.matcher(link.strip());
        if (!matcher.find()) {
            throw new IllegalArgumentException(
                    MessageFormat.format("Playlist link with invalid form encountered: {0}", link));
        }
        return matcher.group(PLAYLIST_ID_CAPTURING_GROUP_NAME);
    }
}
