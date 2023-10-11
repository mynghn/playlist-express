package mynghn.youtube.message.search.request;

import lombok.Builder;
import mynghn.youtube.enums.Topic;
import mynghn.youtube.enums.VideoCategory;

@Builder(builderMethodName = "fullBuilder")
public record YouTubeSearchQueryParams(String part,
                                       String type,
                                       String topicId,
                                       String videoCategoryId,
                                       String q,
                                       int maxResults) {

    @Builder
    public YouTubeSearchQueryParams(String topicId,
            String videoCategoryId,
            String q,
            int maxResults) {
        this("snippet", "video",
                topicId, videoCategoryId, q, maxResults);
    }

    @Builder(builderMethodName = "musicQueryBuilder")
    public YouTubeSearchQueryParams(String q, int maxResults) {
        this(Topic.MUSIC.getId(), VideoCategory.MUSIC.getId(), q, maxResults);
    }
}
