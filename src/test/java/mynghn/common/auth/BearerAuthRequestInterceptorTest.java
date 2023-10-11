package mynghn.common.auth;

import static org.junit.jupiter.api.Assertions.assertEquals;

import feign.RequestTemplate;
import org.junit.jupiter.api.Test;

class BearerAuthRequestInterceptorTest {

    @Test
    void bearerAuthorizationHeaderWithReceivedTokenIsInjectedToRequestTemplate() {
        // Arrange
        final String testToken = "test-token";
        BearerAuthRequestInterceptor sut = new BearerAuthRequestInterceptor(testToken);
        final RequestTemplate testRequestTemplate = new RequestTemplate();

        // Act
        sut.apply(testRequestTemplate);

        // Assert
        Object actualAuthorizationHeaderVal = testRequestTemplate.headers().get("Authorization")
                .toArray()[0];
        String expectedAuthorizationHeaderVal = String.format("Bearer %s", testToken);
        assertEquals(expectedAuthorizationHeaderVal, actualAuthorizationHeaderVal);
    }
}
