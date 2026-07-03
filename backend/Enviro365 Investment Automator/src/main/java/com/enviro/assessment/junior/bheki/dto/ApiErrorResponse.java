package com.enviro.assessment.junior.bheki.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ApiErrorResponse {

    private String message;
    private LocalDateTime timestamp;
    private int status;
}
