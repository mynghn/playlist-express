package mynghn.youtube.util;

public class YouTubePlaylistLinkBuilder {

    private static final String LINK_TEMPLATE = "https://www.youtube.com/playlist?list=%s";

    public static String build(String playlistId) {
        return String.format(LINK_TEMPLATE, playlistId);
    }

}
