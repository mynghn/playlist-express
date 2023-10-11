package mynghn.youtube.util;

import mynghn.spotify.model.Track;
import mynghn.youtube.message.search.response.YouTubeSearchResult;

public class YouTubeSearchResultEvaluator {

    /**
     * Check if given YouTube search result is exactly about given Spotify Track
     *
     * @param searchResult YouTube search result to substitute for source Spotify Track
     * @param source Spotify Track obj currently finding its video counterpart from YouTube
     * @return if the YouTube search result is exactly about source Spotify Track
     */
    public static boolean match(YouTubeSearchResult searchResult, Track source) {
        // FIXME: Implement logics
        return true;
    }
}
