package com.spring.batch.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/batch")
@RequiredArgsConstructor
public class BatchJobController {

    private final Job job;
    private final JobLauncher jobLauncher;

    @GetMapping("/launchJob")
    public String launchJob() {
        try {
            JobParameters jobParameters = new JobParametersBuilder()
                    .addLong("time", System.currentTimeMillis()) // Unique job parameter
                    .toJobParameters();

            jobLauncher.run(job, jobParameters);
            return "Batch job launched successfully";
        } catch (Exception e) {
            return "Error launching batch job: " + e.getMessage();
        }
    }


}
