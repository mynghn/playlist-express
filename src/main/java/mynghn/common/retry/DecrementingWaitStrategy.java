package mynghn.common.retry;

import com.github.rholder.retry.Attempt;
import com.github.rholder.retry.WaitStrategy;
import java.text.MessageFormat;
import lombok.Builder;

public class DecrementingWaitStrategy implements WaitStrategy {

    private final long initialSleepTime;
    private final long decrement;
    private final long minWaitTime;

    @Builder
    public DecrementingWaitStrategy(long initialSleepTime, long decrement, long minWaitTime) {
        if (initialSleepTime < 0L) {
            throw new IllegalArgumentException(
                    MessageFormat.format("initialSleepTime must be >= 0 but is {0}",
                            initialSleepTime));
        }
        if (decrement < 0L) {
            throw new IllegalArgumentException(
                    MessageFormat.format("decrement must be >= 0 but is {0}",
                            initialSleepTime));
        }
        if (minWaitTime < 0L) {
            throw new IllegalArgumentException(
                    MessageFormat.format("minWaitTime must be >= 0 but is {0}",
                            initialSleepTime));
        }

        this.initialSleepTime = initialSleepTime;
        this.decrement = decrement;
        this.minWaitTime = minWaitTime;
    }

    @Override
    public long computeSleepTime(Attempt failedAttempt) {
        long result = initialSleepTime - (decrement * (failedAttempt.getAttemptNumber() - 1));
        return Math.max(result, minWaitTime);
    }
}
