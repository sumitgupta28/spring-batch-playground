package com.spring.batch.dto;

import lombok.Data;

@Data
public class JobParamsRequest {
    private String paramKey;

    private String paramValue;
}
