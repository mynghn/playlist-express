package mynghn.youtube.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.github.rholder.retry.RetryException;
import com.github.rholder.retry.RetryerBuilder;
import com.github.rholder.retry.StopStrategies;
import feign.FeignException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.stream.IntStream;
import mynghn.common.exception.PollingException;
import mynghn.youtube.message.auth.response.YouTubeAuthTokenResponse;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class YouTubeAuthPollingAgentTest {

    private final YouTubeAuthTokenResponse dummyTokenResponse = Mockito.mock(
            YouTubeAuthTokenResponse.class);
    private final FeignException dummyFeignException = Mockito.mock(FeignException.class);

    @Test
    void pollingSucceedsWithRetry() {
        // Arrange
        final var callCountWrapper = new Object() {
            int cnt;
        };

        final Callable<YouTubeAuthTokenResponse> stubTokenRequestCall = () -> {
            callCountWrapper.cnt++;
            if (callCountWrapper.cnt < 2) {
                throw dummyFeignException;
            }
            return dummyTokenResponse;
        };

        final YouTubeAuthPollingAgent sut = new YouTubeAuthPollingAgent(stubTokenRequestCall);

        // Act & Assert
        assertNotNull(sut.poll(0));
        assertEquals(2, callCountWrapper.cnt);
    }

    @Test
    void pollingStartsAfterInitialSleepTime() {
        // Arrange
        final long testStartAfterInMillis = 3 * 1000L;

        final var timerWrapper = new Object() {
            LocalDateTime firstCalledAt;
        };

        final Callable<YouTubeAuthTokenResponse> stubTokenRequestCall = () -> {
            timerWrapper.firstCalledAt = LocalDateTime.now();
            return dummyTokenResponse;
        };

        final YouTubeAuthPollingAgent sut = new YouTubeAuthPollingAgent(stubTokenRequestCall);

        // Act
        final LocalDateTime executionStartedAt = LocalDateTime.now();
        sut.poll(testStartAfterInMillis);

        // Assert
        final LocalDateTime expectedFirstExecutionTimeInRough = executionStartedAt
                .plus(testStartAfterInMillis, ChronoUnit.MILLIS)
                .truncatedTo(ChronoUnit.SECONDS);
        final LocalDateTime actualFirstExecutionTimeInRough = timerWrapper.firstCalledAt
                .truncatedTo(ChronoUnit.SECONDS);

        assertEquals(expectedFirstExecutionTimeInRough, actualFirstExecutionTimeInRough);
    }

    @Test
    void waitIntervalDecreasesByFixedAmount() {
        // Arrange
        final var callHistory = new Object() {
            final List<LocalDateTime> calledTimeList = new ArrayList<>();
            int cnt;
        };

        final Callable<YouTubeAuthTokenResponse> stubTokenRequestCall = () -> {
            callHistory.cnt++;
            callHistory.calledTimeList.add(LocalDateTime.now());
            if (callHistory.cnt < 5) {
                throw dummyFeignException;
            }
            return dummyTokenResponse;
        };

        final YouTubeAuthPollingAgent sut = new YouTubeAuthPollingAgent(stubTokenRequestCall);

        // Act
        sut.poll(0L);

        // Assert
        final long intervalDecrementInSeconds = 1L;
        final long initialWaitInSeconds = 5L;

        IntStream.range(1, callHistory.calledTimeList.size()).forEach(idx -> {
            LocalDateTime prevCallTime = callHistory.calledTimeList.get(idx - 1);
            LocalDateTime nextCallTime = callHistory.calledTimeList.get(idx);

            assertEquals(initialWaitInSeconds - (intervalDecrementInSeconds * (idx - 1)),
                    ChronoUnit.SECONDS.between(prevCallTime, nextCallTime));
        });
    }

    @Test
    void waitsNoLessThanMinimumIntervalTime() {
        // Arrange
        final var callHistory = new Object() {
            final List<LocalDateTime> calledTimeList = new ArrayList<>();
            int cnt;
        };

        final Callable<YouTubeAuthTokenResponse> stubTokenRequestCall = () -> {
            callHistory.cnt++;
            callHistory.calledTimeList.add(LocalDateTime.now());
            if (callHistory.cnt < 10) {
                throw dummyFeignException;
            }
            return dummyTokenResponse;
        };

        final YouTubeAuthPollingAgent sut = new YouTubeAuthPollingAgent(stubTokenRequestCall);

        // Act
        sut.poll(0L);

        // Assert
        final long minIntervalInSeconds = 1L;

        IntStream.range(1, callHistory.calledTimeList.size()).forEach(idx -> {
            LocalDateTime prevCallTime = callHistory.calledTimeList.get(idx - 1);
            LocalDateTime nextCallTime = callHistory.calledTimeList.get(idx);

            assertTrue(
                    ChronoUnit.SECONDS.between(prevCallTime, nextCallTime) >= minIntervalInSeconds);
        });
    }

    @Test
    void pollingExceptionWithRetryExceptionCauseThrownWhenPollingNeverSucceededUntilTimeout() {
        // Arrange
        final Callable<YouTubeAuthTokenResponse> stubTokenRequestCall = () -> {
            throw dummyFeignException;
        };

        final YouTubeAuthPollingAgent sut = new YouTubeAuthPollingAgent(stubTokenRequestCall,
                RetryerBuilder.<YouTubeAuthTokenResponse>newBuilder()
                        .retryIfExceptionOfType(FeignException.class)
                        .withStopStrategy(StopStrategies.stopAfterAttempt(10))
                        .build());

        // Act & Assert
        try {
            sut.poll(0);
        } catch (Throwable t) {
            assertInstanceOf(PollingException.class, t);
            assertInstanceOf(RetryException.class, t.getCause());
        }
    }

    @Test
    void pollingExceptionWithExecutionExceptionCauseThrownWhenCheckedExceptionOccurredDuringPolling() {
        // Arrange
        final var callCountWrapper = new Object() {
            int cnt;
        };

        final Callable<YouTubeAuthTokenResponse> stubTokenRequestCall = () -> {
            callCountWrapper.cnt++;
            if (callCountWrapper.cnt == 1) {
                throw dummyFeignException;
            }
            throw new Exception(); // throw checked exception
        };

        final YouTubeAuthPollingAgent sut = new YouTubeAuthPollingAgent(stubTokenRequestCall,
                RetryerBuilder.<YouTubeAuthTokenResponse>newBuilder()
                        .retryIfExceptionOfType(FeignException.class)
                        .withStopStrategy(StopStrategies.stopAfterAttempt(10))
                        .build());

        // Act & Assert
        try {
            sut.poll(0);
        } catch (Throwable t) {
            assertInstanceOf(PollingException.class, t);
            assertInstanceOf(ExecutionException.class, t.getCause());
        }
    }
}
