package net.catenax.semantics.registry.dto;

import lombok.Value;

@Value
public class BatchResultDto {
    String message;
    String idExternal;
    Integer status;
}
