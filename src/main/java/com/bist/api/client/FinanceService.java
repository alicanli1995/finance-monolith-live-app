package com.bist.api.client;


import com.bist.api.rest.dto.FinanceApiDTO;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@FeignClient(value = "finance",
        url = "https://www.isyatirim.com.tr/_layouts/15/Isyatirim.Website/Common/Data.aspx/OneEndeks?endeks=")
public interface FinanceService {
    @GetMapping
    @Retry(name = "finance")
    @CircuitBreaker(name = "finance", fallbackMethod = "getBISTInformationFallback")
    List<FinanceApiDTO> getBISTInformation(@RequestParam String endeks);

    default List<FinanceApiDTO> getBISTInformationFallback(String endeks, Throwable t) {
        return new ArrayList<>();
    }

}