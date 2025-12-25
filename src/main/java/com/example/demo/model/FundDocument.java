package com.example.demo.model;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.math.BigDecimal;

//elastic entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "funds")
public class FundDocument {

    @Id
    private String id;

    @Field(type = FieldType.Text, analyzer = "standard")
    private String fundCode;

    @Field(type = FieldType.Text, analyzer = "standard")
    private String fundName;

    @Field(type = FieldType.Keyword)
    private String umbrellaType;

    // double for fast queries
    @Field(type = FieldType.Double)
    private BigDecimal return1Month;

    @Field(type = FieldType.Double)
    private BigDecimal return3Month;

    @Field(type = FieldType.Double)
    private BigDecimal return6Month;

    @Field(type = FieldType.Double)
    private BigDecimal returnYtd;

    @Field(type = FieldType.Double)
    private BigDecimal return1Year;

    @Field(type = FieldType.Double)
    private BigDecimal return5Year;

}
