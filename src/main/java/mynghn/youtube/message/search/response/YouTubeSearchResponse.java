package mynghn.youtube.message.search.response;

import java.util.List;
import mynghn.youtube.message.YouTubePaginationInfo;

public record YouTubeSearchResponse(String regionCode,
                                    String nextPageToken,
                                    YouTubePaginationInfo pageInfo,
                                    List<YouTubeSearchResult> items) {


}
