package com.example.demo.service.strategy;

import com.example.demo.model.FundSearchRequest;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class TextSearchStrategy implements SearchFilterStrategy{
    @Override
    public void apply(Criteria criteria, FundSearchRequest request) {

        if (StringUtils.hasText(request.getQuote())) {
            criteria.subCriteria(
                    new Criteria("fundName").contains(request.getQuote())
                            .or("fundCode").contains(request.getQuote())
            );
        }
    }
}
