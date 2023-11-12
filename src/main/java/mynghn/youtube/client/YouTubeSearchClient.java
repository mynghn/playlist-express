package mynghn.youtube.client;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import feign.Feign;
import feign.Headers;
import feign.QueryMap;
import feign.Request;
import feign.RequestLine;
import feign.gson.DoubleToIntMapTypeAdapter;
import feign.gson.GsonDecoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import mynghn.common.gson.deserializer.LocalDateTimeDeserializer;
import mynghn.youtube.deserializer.YouTubeResourceIdDeserializer;
import mynghn.youtube.enums.BaseUrl;
import mynghn.youtube.enums.EndPointTemplates;
import mynghn.youtube.message.YouTubeResourceId;
import mynghn.youtube.message.search.request.YouTubeSearchQueryParams;
import mynghn.youtube.message.search.response.YouTubeSearchResponse;

public interface YouTubeSearchClient {

    int DEFAULT_PAGE_SIZE = 25;

    /**
     * Build Feign client with API key embedded in query param.
     *
     * @param apiKey API key of a registered Google Cloud project
     * @return Properly configured YouTubeSearchClient Feign client instance
     */
    static YouTubeSearchClient connect(String apiKey) {
        return Feign.builder()
                // TODO: Add error decoder
                .requestInterceptor(new ApiKeyEmbedRequestInterceptor(apiKey))
                .decoder(new GsonDecoder(new GsonBuilder()
                        .setPrettyPrinting()
                        .registerTypeAdapter(new TypeToken<Map<String, Object>>() {
                                }.getType(),
                                new DoubleToIntMapTypeAdapter())
                        .registerTypeAdapter(YouTubeResourceId.class,
                                new YouTubeResourceIdDeserializer())
                        .registerTypeAdapter(LocalDateTime.class,
                                new LocalDateTimeDeserializer(DateTimeFormatter.ISO_DATE_TIME))
                        .create()))
                .options(new Request.Options())
                .target(YouTubeSearchClient.class, BaseUrl.YOUTUBE_DATA_API_V3.getValue());
    }

    /**
     * Search resources from YouTube
     *
     * @param params Request query parameters indicating search specifications
     * @return API response with search results
     */
    @RequestLine(EndPointTemplates.SEARCH_LIST)
    @Headers("Accept: application/json")
    YouTubeSearchResponse search(@QueryMap YouTubeSearchQueryParams params);

    /**
     * Search videos especially within area of <i>music</i> from YouTube
     *
     * @param query    Search query to find videos about
     * @param pageSize Maximum # of search results in a page
     * @return API response with search results
     */
    default YouTubeSearchResponse searchMusic(String query, int pageSize) {
        return search(YouTubeSearchQueryParams.musicQueryBuilder()
                .q(query)
                .maxResults(pageSize)
                .build());
    }

    /**
     * Search videos especially within area of <i>music</i> from YouTube (w/ default page size)
     *
     * @param query Search query to find videos about
     * @return API response with search results
     */
    default YouTubeSearchResponse searchMusic(String query) {
        return searchMusic(query, DEFAULT_PAGE_SIZE);
    }
}
