package com.example.demo.service;

import com.example.demo.model.Fund;
import com.example.demo.model.FundDocument;
import com.example.demo.repository.FundElasticRepository;
import com.example.demo.repository.FundRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
@Slf4j
public class FundDataManager {

    private final FundRepository fundRepository;
    private final FundElasticRepository fundElasticRepository;
    private final ElasticsearchOperations elasticsearchOperations;

    @Transactional
    public List<Fund> saveAllToPostgres(List<Fund> funds) {
        List<Fund> savedFunds = fundRepository.saveAll(funds);
        log.info("DB Transaction has been completed {} records have been saved", savedFunds.size());
        return savedFunds;
    }

    public List<FundDocument> saveAllToElastic(List<FundDocument> fundDocuments) {
        Iterable<FundDocument> result = fundElasticRepository.saveAll(fundDocuments);

        List<FundDocument> savedDocs = StreamSupport.stream(result.spliterator(), false).toList();

        log.info("Elastic Transaction has been completed {} records have been saved", savedDocs.size());

        return savedDocs;
    }
    //Elastic Read Only
    public Page<FundDocument> executeSearch(Criteria criteria, Pageable pageable) {

        CriteriaQuery query = new CriteriaQuery(criteria).setPageable(pageable);

        SearchHits<FundDocument> searchHits = elasticsearchOperations.search(query, FundDocument.class);

        List<FundDocument> list = searchHits.stream()
                .map(SearchHit::getContent)
                .collect(Collectors.toList());

        return PageableExecutionUtils.getPage(list, pageable, searchHits::getTotalHits);
    }
}
