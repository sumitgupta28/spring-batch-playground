package com.spring.batch.config;

import com.spring.batch.dto.Person;
import com.spring.batch.listener.CustomJobExecutionListener;
import com.spring.batch.listener.CustomStepExecutionListener;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.support.DefaultBatchConfiguration;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.ResourcelessJobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.batch.item.file.transform.FieldExtractor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
public class BatchConfiguration extends DefaultBatchConfiguration {

    private final CustomJobExecutionListener customJobExecutionListener;
    private final CustomStepExecutionListener customStepExecutionListener;

    // For Spring Batch 5+
    @Bean
    public JobRepository jobRepository() {
        return new ResourcelessJobRepository();
    }

    @Bean
    public TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5); // Number of threads to keep in the pool
        executor.setMaxPoolSize(10); // Maximum number of threads in the pool
        executor.setQueueCapacity(25); // Capacity of the queue for tasks
        executor.setThreadNamePrefix("batch-thread-"); // Prefix for thread names
        executor.initialize();
        return executor;
    }


    // Item Reader
    @Bean
    public FlatFileItemReader<Person> reader() {
        return new FlatFileItemReaderBuilder<Person>()
                .name("personItemReader")
                .resource(new ClassPathResource("sample-data.csv")) // Input file
                .delimited()
                .names("index", "userId", "firstName", "lastName", "sex", "email", "phone", "dateOfBirth", "jobTitle")
                .fieldSetMapper(new BeanWrapperFieldSetMapper<>() {{
                    setTargetType(Person.class);
                }})
                .build();
    }

    // Item Processor (Optional, for transforming data)
    @Bean
    public ItemProcessor<Person, Person> processor() {
        return person -> {
            // Example: Convert names to uppercase
            person.setIndex(person.getIndex());
            person.setIndex(person.getUserID());
            person.setFirstName(person.getFirstName().toUpperCase());
            person.setLastName(person.getLastName().toUpperCase());
            return person;
        };
    }

    // Item Writer
    @Bean
    public FlatFileItemWriter<Person> writer() {
        return new FlatFileItemWriterBuilder<Person>()
                .name("personItemWriter")
                .resource(new FileSystemResource("output-data.csv")) // Output file
                .lineAggregator(new DelimitedLineAggregator<>() {{
                    setDelimiter(",");
                    setFieldExtractor((FieldExtractor<Person>) item -> new Object[]{item.getIndex(), item.getUserID(), item.getFirstName(), item.getLastName()});
                }})
                .build();
    }

    // Step Definition
    @Bean
    public Step defineJobSteps(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("importStep", jobRepository)
                .<Person, Person>chunk(20000, transactionManager) // Chunk size
                .reader(reader())
                .processor(processor()) // Optional
                .writer(writer())
                // .taskExecutor(taskExecutor())
                .listener(customStepExecutionListener)
                .build();
    }

    // Job Definition
    @Bean
    public Job importUserJob(JobRepository jobRepository, Step defineJobSteps) {
        return new JobBuilder("importUserJob", jobRepository)
                .start(defineJobSteps)
                .listener(customJobExecutionListener)
                .build();
    }
}
