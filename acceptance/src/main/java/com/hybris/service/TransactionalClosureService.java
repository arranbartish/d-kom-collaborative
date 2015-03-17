package com.hybris.service;

import com.hybris.service.exception.FailureException;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Seconds;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

import static java.lang.Thread.sleep;
import static org.slf4j.LoggerFactory.getLogger;

@Service
@Transactional
public class TransactionalClosureService implements ClosureService {

    private static final long SLEEP_INTERVAL = TimeUnit.MILLISECONDS.toMillis(150);
    private static final Logger LOG = getLogger(TransactionalClosureService.class);

    @Override
    public void execute(Closure closure) throws Exception {
        closure.execute();
    }

    @Override
    public <T> T execute(ValueReturningClosure<T> closure) throws Exception {
        return closure.execute();
    }


    public void waitForCondition(final ConditionChecker checker) {
        DateTime startTime = DateTime.now();

        DateTime exitTime = DateTime.now().plusSeconds(checker.getTimeoutInSeconds());

        int attemptCount = 0;

        try {
            while (DateTime.now().isBefore(exitTime)) {

                attemptCount++;

                String context = StringUtils.defaultString(checker.getName());

                if (checker.isConditionFailed()) {
                    long elapsedTime = Seconds.secondsBetween(startTime, DateTime.now()).getSeconds();

                    String logMsg = "ConditionChecker " + checker.getName() + " FAILED [attempts:" + attemptCount
                            + ",time:" + elapsedTime + "]";
                    if (checker.getName() != null)
                        logMsg += "[context: " + context + "]";

                    LOG.debug(logMsg);
                    break;
                }

                if (checker.isConditionPassed()) {
                    long elapsedTime = Seconds.secondsBetween(startTime, DateTime.now()).getSeconds();

                    String logMsg = "ConditionChecker " + checker.getName() + " passed [attempts:" + attemptCount
                            + ",time:" + elapsedTime + "]";
                    if (checker.getName() != null)
                        logMsg += "[context: " + context + "]";
                    LOG.debug(logMsg);
                    return;
                }

                sleep(SLEEP_INTERVAL);
            }
        } catch (Exception e) {
            LOG.info("Unhandled exception: " + checker.getErrorMessage(), e);
            throw new FailureException("Unhandled exception: " + checker.getErrorMessage(), e);
        }
    }
}
