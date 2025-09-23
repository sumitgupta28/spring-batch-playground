package com.spring.batch.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CustomStepExecutionListener implements StepExecutionListener {

    @Override
    public void beforeStep(StepExecution stepExecution) {
        log.info("Before step:  {} ", stepExecution.getStepName());
        // Perform any setup or initialization logic here
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        log.info("After step: {} status {} ", stepExecution.getStepName(), stepExecution.getExitStatus());
        // Perform any cleanup or finalization logic here
        return stepExecution.getExitStatus(); // Return the original exit status or a modified one
    }
}
