package com.example.demo.service.strategy;

import com.example.demo.model.FundSearchRequest;
import org.springframework.data.elasticsearch.core.query.Criteria;

//tek bir search parametresi içinde if blokları yerine, criteria ile open/closed prensibine uyan strategy
public interface SearchFilterStrategy {
    void apply(Criteria criteria, FundSearchRequest request);
}
