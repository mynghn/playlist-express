package mynghn.youtube.polling;

import com.github.rholder.retry.RetryException;
import com.github.rholder.retry.Retryer;
import com.github.rholder.retry.RetryerBuilder;
import com.github.rholder.retry.StopStrategies;
import feign.FeignException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import lombok.Builder;
import mynghn.common.exception.PollingException;
import mynghn.common.retry.DecrementingWaitStrategy;
import mynghn.youtube.client.YouTubeAuthClient;
import mynghn.youtube.message.auth.response.YouTubeAuthTokenResponse;

public class YouTubeAuthPollingAgent {

    // Wait strategy
    private static final long START_AFTER_IN_MILLS = 5 * 1000L;
    private static final long INTERVAL_DECREMENT_IN_MILLIS = 1000L;
    private static final long MIN_INTERVAL_IN_MILLIS = 1000L;

    // Stop strategy
    private static final long TIME_OUT_IN_MILLIS = 3 * 60 * 1000L;

    private final Retryer<YouTubeAuthTokenResponse> retryer;
    private final Callable<YouTubeAuthTokenResponse> tokenRequestCall;

    @Builder
    public YouTubeAuthPollingAgent(String clientId, String clientSecret, String deviceCode,
            YouTubeAuthClient authClient) {
        // build retryer
        retryer = RetryerBuilder.<YouTubeAuthTokenResponse>newBuilder()
                .retryIfExceptionOfType(FeignException.class)
                .withWaitStrategy(DecrementingWaitStrategy.builder()
                        .initialSleepTime(START_AFTER_IN_MILLS)
                        .decrement(INTERVAL_DECREMENT_IN_MILLIS)
                        .minWaitTime(MIN_INTERVAL_IN_MILLIS)
                        .build())
                .withStopStrategy(
                        StopStrategies.stopAfterDelay(TIME_OUT_IN_MILLIS, TimeUnit.MILLISECONDS))
                .build();

        // token request call
        tokenRequestCall = () -> authClient.obtainToken(clientId, clientSecret, deviceCode);
    }

    public YouTubeAuthTokenResponse poll() throws PollingException {
        try {
            return retryer.call(tokenRequestCall);
        } catch (ExecutionException e) {
            throw new PollingException("Aborted with unexpected exception.", e);
        } catch (RetryException e) {
            throw new PollingException("Polling never succeeded.", e);
        }
    }
}
