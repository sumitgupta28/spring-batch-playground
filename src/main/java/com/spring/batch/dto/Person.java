package com.spring.batch.dto;

import lombok.*;
import org.apache.commons.lang3.builder.ToStringBuilder;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class Person {
    private String index;
    private String userID;
    private String firstName;
    private String lastName;
    private String sex;
    private String email;
    private String phone;
    private String dateOfBirth;
    private String jobTitle;


    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append(index)
                .append(userID)
                .append(firstName)
                .append(lastName)
                .append(sex)
                .append(email)
                .append(phone)
                .append(dateOfBirth)
                .append(jobTitle)
                .toString();
    }
}
