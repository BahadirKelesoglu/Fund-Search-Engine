package com.example.demo.service.strategy;

import com.example.demo.model.FundSearchRequest;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class RateStrategy implements SearchFilterStrategy{
    @Override
    public void apply(Criteria criteria, FundSearchRequest request) {

        if (request.getMinReturn() != null && StringUtils.hasText(request.getReturnPeriod())) {
            criteria.subCriteria(
                    new Criteria(request.getReturnPeriod()).greaterThanEqual(request.getMinReturn())
            );
        }
    }
}
