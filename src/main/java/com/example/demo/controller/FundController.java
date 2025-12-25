package com.example.demo.controller;

import com.example.demo.model.FundDocument;
import com.example.demo.model.FundSearchRequest;
import com.example.demo.service.FundService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/funds")
@RequiredArgsConstructor
public class FundController {
    private final FundService fundService;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadExcel(@RequestParam("file") MultipartFile file) {
        fundService.importFundsFromExcel(file);
        return ResponseEntity.ok("File Successfully Imported");
    }

    //pagination and filtering options
    @GetMapping("/search")
    public ResponseEntity<Page<FundDocument>> searchFunds(@RequestParam(required = false) String quote, //fund code or fund name
                                                          @RequestParam(required = false) String umbrellaType,
                                                          @RequestParam(required = false) Double minReturn, //based of return period
                                                          @RequestParam(required = false) String returnPeriod,
                                                          @PageableDefault(size = 10, sort = "fundName") Pageable pageable) {

        FundSearchRequest request = new FundSearchRequest();
        request.setQuote(quote);
        request.setUmbrellaType(umbrellaType);
        request.setMinReturn(minReturn);
        request.setReturnPeriod(returnPeriod);

        return ResponseEntity.ok(fundService.searchFundsDynamic(request, pageable));
    }
}
