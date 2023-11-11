package mynghn.youtube.util;

import mynghn.youtube.model.YouTubePlaylist;

public class YouTubePlaylistLinkBuilder {

    private static final String LINK_TEMPLATE = "https://www.youtube.com/playlist?list=%s";

    public static String build(YouTubePlaylist playlist) {
        return String.format(LINK_TEMPLATE, playlist.id());
    }
}
