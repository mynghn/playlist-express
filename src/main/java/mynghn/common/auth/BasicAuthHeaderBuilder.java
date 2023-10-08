package mynghn.common.auth;

import java.nio.charset.Charset;
import java.text.MessageFormat;
import java.util.Base64;

public class BasicAuthHeaderBuilder {

    private static final Base64.Encoder encoder = Base64.getEncoder();

    /**
     * Build header value for HTTP Basic authentication with JVM default charset
     *
     * @param username username to be encoded in the first half of the header value
     * @param password password to be encoded in the second half of the header value
     * @return Authorization header value for HTTP Basic authentication
     */
    public static String build(String username, String password) {
        return build(username, password, Charset.defaultCharset());
    }

    /**
     * Build header value for HTTP Basic authentication with given charset
     *
     * @param username username to be encoded in the first half of the header value
     * @param password password to be encoded in the second half of the header value
     * @param charset  charset to be specified on Base64 encoding
     * @return Authorization header value for HTTP Basic authentication
     */
    public static String build(String username, String password, Charset charset) {
        return String.format("Basic %s",
                base64Encode(combineUsernameAndPassword(username, password), charset));
    }

    private static String combineUsernameAndPassword(String username, String password) {
        return MessageFormat.format("{0}:{1}", username, password);
    }

    private static String base64Encode(String src, Charset charset) {
        return new String(encoder.encode(src.getBytes(charset)), charset);
    }
}
