package com.spring.batch.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.support.DefaultBatchConfiguration;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.ResourcelessJobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.Arrays;
import java.util.List;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class BatchConfiguration extends DefaultBatchConfiguration {


    private static final int BATCH_SIZE = 3;
    private final PlatformTransactionManager platformTransactionManager;


    // For Spring Batch 5+
    @Bean
    public JobRepository jobRepository() {
        return new ResourcelessJobRepository();
    }


    /**
     * Job which contains multiple steps
     */
    @Bean
    public Job firstJob() {
        return new JobBuilder("first job", jobRepository())
                .incrementer(new RunIdIncrementer())
                .start(chunkStep())
                .next(taskletStep())
                .build();
    }

    @Bean
    public Step taskletStep () {
        return new StepBuilder("first step", jobRepository())
                .tasklet((stepContribution, chunkContext) -> {
                    log.info("This is first tasklet step");
                    log.info("SEC = {}", chunkContext.getStepContext().getStepExecutionContext());
                    return RepeatStatus.FINISHED;
                }, platformTransactionManager).build();
    }

    @Bean
    public Step chunkStep() {
        return new StepBuilder("first step", jobRepository())
                .<String, String>chunk(BATCH_SIZE, platformTransactionManager)
                .reader(reader())
                .writer(writer())
                .build();
    }

    @Bean
    public ItemReader<String> reader() {
        List<String> data = Arrays.asList("Byte", "Code", "Data", "Disk", "File", "Input", "Loop", "Logic", "Mode", "Node");
        return new ListItemReader<>(data);
    }

    @Bean
    public ItemWriter<String> writer() {
        return items -> {
            for (String item : items) {
                log.info("Writing item: {}", item);
            }
            log.info("------------ BATCH_SIZE: {} documents written. ------------",BATCH_SIZE);
        };
    }


}
