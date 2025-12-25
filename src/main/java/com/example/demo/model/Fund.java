package com.example.demo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Fund {
    @Id
    private String fundCode;
    private String fundName;
    private String umbrellaType;

    private BigDecimal return1Month;
    private BigDecimal return3Month;
    private BigDecimal return6Month;
    private BigDecimal returnYtd;
    private BigDecimal return1Year;
    private BigDecimal return5Year;




}
