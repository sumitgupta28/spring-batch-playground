package com.spring.batch.service;

import com.opencsv.CSVWriter;
import com.spring.batch.dto.Person;
import net.datafaker.Faker;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@Service
public class CsvGeneratorService {

    public void generateUsersCsv(String filePath, int numberOfUsers) throws IOException {
        Faker faker = new Faker();
        List<Person> persons = new ArrayList<>();
        for (int i = 0; i < numberOfUsers; i++) {
            persons.add(Person.builder()
                    .index(faker.idNumber().ssnValid())
                    .userID(faker.internet().username())
                    .firstName(faker.name().firstName())
                    .lastName(faker.name().lastName())
                    .sex(faker.gender().types())
                    .email(faker.internet().emailAddress())
                    .phone(faker.phoneNumber().cellPhone())
                    .dateOfBirth(faker.date().birthday(1, 99, "MM-dd-yyyy"))
                    .jobTitle(faker.job().title())
                    .build());
        }
        writeCsvFile(filePath, persons);
    }

    private void writeCsvFile(String filePath, List<Person> personList) throws IOException {
        try (CSVWriter writer = new CSVWriter(new FileWriter(filePath), CSVWriter.DEFAULT_SEPARATOR,
                CSVWriter.NO_QUOTE_CHARACTER,
                CSVWriter.DEFAULT_ESCAPE_CHARACTER,
                CSVWriter.RFC4180_LINE_END)) {
            // Write header
            String[] header = {"Index", "User Id", "First Name", "Last Name", "Sex", "Email", "Phone", "Date of birth", "Job Title"};

            writer.writeNext(header);

            // Write data rows
            for (Person person : personList) {
                String[] rowData = {
                        person.getIndex(),
                        person.getUserID(),
                        person.getFirstName(),
                        person.getLastName(),
                        person.getSex(),
                        person.getEmail(),
                        person.getPhone(),
                        person.getDateOfBirth(),
                        person.getJobTitle()
                };
                writer.writeNext(rowData);
            }
        }
    }
}
