package com.spring.batch.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@Component
@Slf4j
public class CustomJobExecutionListener implements JobExecutionListener {

    private StopWatch stopWatch;

    @Override
    public void beforeJob(JobExecution jobExecution) {
        stopWatch = new StopWatch();
        stopWatch.start();
        log.info("[beforeJob]Job  Status : {}  {} ", jobExecution.getStatus(), jobExecution.getCreateTime());
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        try {
            if (jobExecution.getStatus().isUnsuccessful()) {
                // Log the error or take necessary actions
                log.info("Job failed with the following exceptions: " + jobExecution.getAllFailureExceptions());
            }
        } finally {
            stopWatch.stop();
            log.info("[afterJob]Job  Status : {} execution time  {} ", jobExecution.getStatus(), stopWatch.getTotalTimeMillis());
        }


    }

}
