package mynghn.common.auth;

import feign.RequestInterceptor;
import feign.RequestTemplate;

/**
 * An interceptor that adds the request header needed to use OAuth 2.0 Bearer authentication.
 */
public class BearerAuthRequestInterceptor implements RequestInterceptor {

    private final String headerValue;

    public BearerAuthRequestInterceptor(String token) {
        this.headerValue = String.format("Bearer %s", token);
    }

    @Override
    public void apply(RequestTemplate template) {
        template.header("Authorization", headerValue);
    }
}

