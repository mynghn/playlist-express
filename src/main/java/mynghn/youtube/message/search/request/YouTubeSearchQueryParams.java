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

    private static final String DEFAULT_PART = "snippet";
    private static final String DEFAULT_TYPE = "video";

    public YouTubeSearchQueryParams(String topicId,
            String videoCategoryId,
            String q,
            int maxResults) {
        this(DEFAULT_PART, DEFAULT_TYPE,
                topicId, videoCategoryId, q, maxResults);
    }

    public static YouTubeSearchQueryParamsBuilder builder() {
        return YouTubeSearchQueryParams.fullBuilder()
                .part(DEFAULT_PART)
                .type(DEFAULT_TYPE);
    }

    public YouTubeSearchQueryParams(String q, int maxResults) {
        this(Topic.MUSIC.getId(), VideoCategory.MUSIC.getId(), q, maxResults);
    }

    public static YouTubeSearchQueryParamsBuilder musicQueryBuilder() {
        return YouTubeSearchQueryParams.builder()
                .topicId(Topic.MUSIC.getId())
                .videoCategoryId(VideoCategory.MUSIC.getId());
    }
}
