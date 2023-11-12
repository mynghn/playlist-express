package mynghn.youtube.message.search.response;

import java.time.LocalDateTime;

public record YouTubeSearchResultSnippet(String title,
                                         String description,
                                         String channelId,
                                         String channelTitle,
                                         LocalDateTime publishedAt) {

}
