package com.spring.batch.controller;

import com.spring.batch.service.JobService;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/batch")
@RequiredArgsConstructor
public class BatchJobController {

    private final JobService jobService;

    @GetMapping("/launchJob")
    public String startJob(@RequestParam(name = "jobName") String jobName) throws Exception {
        jobService.invokeJob(jobName);
        return "Job Started...";
    }


}
