package com.catenax.partsrelationshipservice.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Schema(description = "Error response")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {
    private HttpStatus statusCode;
    private String message;
}
