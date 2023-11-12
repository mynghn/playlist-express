package mynghn.youtube.message.search.request;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

class YouTubeSearchQueryParamsTest {

    @Test
    void musicQueryBuilderPopulatesDefaultValues() {
        YouTubeSearchQueryParams built = YouTubeSearchQueryParams.musicQueryBuilder()
                .build();

        assertNotNull(built.part());
        assertNotNull(built.type());
        assertNotNull(built.topicId());
        assertNotNull(built.videoCategoryId());
    }

    @Test
    void qAndMaxResultsConstructorPopulatesDefaultValues() {
        YouTubeSearchQueryParams constructed = new YouTubeSearchQueryParams("Test Q",
                0);

        assertNotNull(constructed.part());
        assertNotNull(constructed.type());
        assertNotNull(constructed.topicId());
        assertNotNull(constructed.videoCategoryId());
    }
}
