package com.example.demo.model;

import lombok.Data;

@Data
public class FundSearchRequest {
    private String quote;
    private String umbrellaType;
    private Double minReturn;
    private String returnPeriod;
}
