package mynghn.youtube.client;

import feign.RequestInterceptor;
import feign.RequestTemplate;

/**
 * An interceptor that adds the request header needed to use OAuth 2.0 Bearer authentication.
 */
public class ApiKeyEmbedRequestInterceptor implements RequestInterceptor {

    private final String apiKey;

    public ApiKeyEmbedRequestInterceptor(String apiKey) {
        this.apiKey = apiKey;
    }

    @Override
    public void apply(RequestTemplate template) {
        template.query("key", apiKey);
    }
}

