package mynghn.common.auth;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Test;

class BasicAuthHeaderBuilderTest {

    @Test
    void basicAuthHeaderWithReceivedUsernameAndPasswordCorrectlyBuiltAfterUTF_8Encoding() {
        // Arrange
        final String testUsername = "테스트유저";
        final String testPassword = "테스트비밀번호";
        final Charset testCharset = StandardCharsets.UTF_8;
        final String expected = "Basic 7YWM7Iqk7Yq47Jyg7KCAOu2FjOyKpO2KuOu5hOuwgOuyiO2YuA==";

        // Act
        String actual = BasicAuthHeaderBuilder.build(testUsername, testPassword, testCharset);

        // Assert
        assertEquals(expected, actual);
    }

    @Test
    void basicAuthHeaderWithReceivedUsernamePasswordCorrectlyBuiltAfterISO_8859_1Encoding() {
        // Arrange
        final String testUsername = "테스트유저";
        final String testPassword = "테스트비밀번호";
        final Charset testCharset = StandardCharsets.ISO_8859_1;
        final String expected = "Basic Pz8/Pz86Pz8/Pz8/Pw==";

        // Act
        String actual = BasicAuthHeaderBuilder.build(testUsername, testPassword, testCharset);

        // Assert
        assertEquals(expected, actual);
    }
}
