package com.catenax.partsrelationshipservice.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Value;

import java.net.URL;

@Schema(description = "Aspect location data")
@Value
public class Aspect {
    @Schema(description = "Aspect name", example = "CE")
    String name;
    @Schema(description = "URL location of aspect data", example = "http://aspects-url/CE")
    URL url;
}
