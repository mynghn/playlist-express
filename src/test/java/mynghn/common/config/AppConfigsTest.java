package mynghn.common.config;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Objects;
import org.junit.jupiter.api.Test;

class AppConfigsTest {

    @Test
    void configurationsLoadedAndProvided() {
        // Arrange
        final String testConfigKey = "test-config-key";
        final String testConfigValue = "test-config-value";
        final String testConfigFilePath = "/test.properties";

        final AppConfigs sut = new AppConfigs(
                Objects.requireNonNull(getClass().getResource(testConfigFilePath)).getPath());

        // Act
        final String configValueLoaded = sut.get(testConfigKey);

        // Assert
        assertEquals(testConfigValue, configValueLoaded);
    }
}
