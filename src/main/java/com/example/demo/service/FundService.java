package com.example.demo.service;

import com.example.demo.model.Fund;
import com.example.demo.model.FundDocument;
import com.example.demo.model.FundSearchRequest;
import com.example.demo.service.strategy.SearchFilterStrategy;
import com.example.demo.util.ExcelHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class FundService {

    private final FundDataManager fundDataManager; //DAL
    private final List<SearchFilterStrategy> searchStrategies; //strategies for elastic search

    /// Cell hücrelerinin tipine göre ekstra kontrol ile DB ve elasticsearch indexlenmesi
    public void importFundsFromExcel(MultipartFile file) {

        try (Workbook workbook = WorkbookFactory.create(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);
            List<Fund> funds = new ArrayList<>();

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                Fund fund = new Fund();
                //set data for postgresql
                //string values
                fund.setFundCode(ExcelHelper.getStringValue(row.getCell(0)));
                fund.setFundName(ExcelHelper.getStringValue(row.getCell(1)));
                fund.setUmbrellaType(ExcelHelper.getStringValue(row.getCell(2)));

                //decimal values
                fund.setReturn1Month(ExcelHelper.getBigDecimalValue(row.getCell(3)));
                fund.setReturn3Month(ExcelHelper.getBigDecimalValue(row.getCell(4)));
                fund.setReturn6Month(ExcelHelper.getBigDecimalValue(row.getCell(5)));
                fund.setReturnYtd(ExcelHelper.getBigDecimalValue(row.getCell(6)));
                fund.setReturn1Year(ExcelHelper.getBigDecimalValue(row.getCell(7)));
                fund.setReturn5Year(ExcelHelper.getBigDecimalValue(row.getCell(8)));

                funds.add(fund);
            }
            //set data for elastic
            List<FundDocument> searchDocuments = funds.stream().map(f -> {
                FundDocument doc = new FundDocument();
                doc.setId(f.getFundCode());
                doc.setFundCode(f.getFundCode());
                doc.setFundName(f.getFundName());
                doc.setUmbrellaType(f.getUmbrellaType());
                doc.setReturn1Month(f.getReturn1Month());
                doc.setReturn3Month(f.getReturn3Month());
                doc.setReturn6Month(f.getReturn6Month());
                doc.setReturnYtd(f.getReturnYtd());
                doc.setReturn1Year(f.getReturn1Year());
                doc.setReturn5Year(f.getReturn5Year());
                return doc;
            }).toList();

            fundDataManager.saveAllToPostgres(funds);
            fundDataManager.saveAllToElastic(searchDocuments);

        } catch (IOException e) {
            throw new RuntimeException("an error occured while importing funds from the excel file");
        }
    }

    public Page<FundDocument> searchFundsDynamic(FundSearchRequest request, Pageable pageable) {

        Criteria criteria = new Criteria();
        //gather strategies on one criteria
        for (SearchFilterStrategy strategy : searchStrategies) {
            strategy.apply(criteria, request);
        }

        return fundDataManager.executeSearch(criteria, pageable);
    }

}
