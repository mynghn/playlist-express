package mynghn.youtube.message.search.response;

import mynghn.youtube.message.YouTubeResourceId;

public record YouTubeSearchResult(YouTubeResourceId id, YouTubeSearchResultSnippet snippet) {

}
