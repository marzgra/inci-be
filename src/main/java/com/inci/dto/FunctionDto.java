package com.inci.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FunctionDto {
    private Long functionId;
    private String functionName;

    public void setFunctionName(String functionName) {
        this.functionName = functionName.toLowerCase();
    }
}
