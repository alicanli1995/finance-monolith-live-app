package com.bist.api.client;


import com.bist.api.rest.dto.IsYatirimFinanceApiDTO;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@FeignClient(value = "isyatirim-finance",
        url = "https://www.isyatirim.com.tr/_layouts/15/Isyatirim.Website/Common/Data.aspx/OneEndeks?endeks=")
public interface IsYatirimFinanceService {
    @GetMapping
    @Retry(name = "finance")
    @CircuitBreaker(name = "finance", fallbackMethod = "getBISTInformationFallback")
    List<IsYatirimFinanceApiDTO> getBISTInformation(@RequestParam String endeks);

    default List<IsYatirimFinanceApiDTO> getBISTInformationFallback(String endeks, Throwable t) {
        return new ArrayList<>();
    }

}