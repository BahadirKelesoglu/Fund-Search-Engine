package com.example.demo.service.strategy;

import com.example.demo.model.FundSearchRequest;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class UmbrellaTypeStrategy implements SearchFilterStrategy{
    @Override
    public void apply(Criteria criteria, FundSearchRequest request) {

        if (StringUtils.hasText(request.getUmbrellaType())) {
            criteria.subCriteria(
                    new Criteria("umbrellaType").is(request.getUmbrellaType())
            );
        }
    }
}
