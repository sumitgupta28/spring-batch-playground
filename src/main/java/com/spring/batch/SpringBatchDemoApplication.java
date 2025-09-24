package com.spring.batch;

import com.spring.batch.service.CsvGeneratorService;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableBatchProcessing
@EnableScheduling
@RequiredArgsConstructor
@EnableAsync
public class SpringBatchDemoApplication implements ApplicationRunner {

    private final CsvGeneratorService csvGeneratorService;

    public static void main(String[] args) {
        SpringApplication.run(SpringBatchDemoApplication.class, args);
    }


    @Override
    public void run(ApplicationArguments args) throws Exception {
        csvGeneratorService.generateUsersCsv("sample-data.csv", 2000000);
    }
}
