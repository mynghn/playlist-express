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
import lombok.RequiredArgsConstructor;
import mynghn.common.exception.PollingException;
import mynghn.common.retry.DecrementingWaitStrategy;
import mynghn.youtube.client.YouTubeAuthClient;
import mynghn.youtube.message.auth.response.YouTubeAuthTokenResponse;

@RequiredArgsConstructor
public class YouTubeAuthPollingAgent {

    // Start strategy
    private static final long START_AFTER_IN_MILLS = 5 * 1000L;

    // Wait strategy
    private static final long INITIAL_WAIT_IN_MILLS = 5 * 1000L;
    private static final long INTERVAL_DECREMENT_IN_MILLIS = 1000L;
    private static final long MIN_INTERVAL_IN_MILLIS = 1000L;

    // Stop strategy
    private static final long TIME_OUT_IN_MILLIS = 3 * 60 * 1000L;

    private final Callable<YouTubeAuthTokenResponse> tokenRequestCall;
    private final Retryer<YouTubeAuthTokenResponse> retryer;

    @Builder
    public YouTubeAuthPollingAgent(String clientId, String clientSecret, String deviceCode,
            YouTubeAuthClient authClient) {
        // token request call
        tokenRequestCall = () -> authClient.obtainToken(clientId, clientSecret, deviceCode);

        // build retryer
        retryer = buildDefaultRetryer();
    }

    public YouTubeAuthPollingAgent(Callable<YouTubeAuthTokenResponse> tokenRequestCall) {
        this(tokenRequestCall, buildDefaultRetryer());
    }

    private static Retryer<YouTubeAuthTokenResponse> buildDefaultRetryer() {
        return RetryerBuilder.<YouTubeAuthTokenResponse>newBuilder()
                .retryIfExceptionOfType(FeignException.class)
                .withWaitStrategy(DecrementingWaitStrategy.builder()
                        .initialSleepTime(INITIAL_WAIT_IN_MILLS)
                        .decrement(INTERVAL_DECREMENT_IN_MILLIS)
                        .minWaitTime(MIN_INTERVAL_IN_MILLIS)
                        .build())
                .withStopStrategy(
                        StopStrategies.stopAfterDelay(TIME_OUT_IN_MILLIS, TimeUnit.MILLISECONDS))
                .build();
    }

    public YouTubeAuthTokenResponse poll() throws PollingException {
        return poll(START_AFTER_IN_MILLS);
    }

    public YouTubeAuthTokenResponse poll(long startAfterInMills) throws PollingException {
        try {
            if (startAfterInMills > 0) {
                Thread.sleep(startAfterInMills);
            }
            return retryer.call(tokenRequestCall);
        } catch (InterruptedException e) {
            throw new PollingException("Interrupted during initial wait.", e);
        } catch (ExecutionException e) {
            throw new PollingException("Aborted with unexpected exception.", e);
        } catch (RetryException e) {
            throw new PollingException("Polling never succeeded.", e);
        }
    }
}
