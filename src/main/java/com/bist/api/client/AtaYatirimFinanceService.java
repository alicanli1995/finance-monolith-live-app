package com.bist.api.client;

import com.bist.api.rest.dto.AtaYatirimFinanceApiDTO;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "ata-finance",
        url = "https://atayatirim.com.tr/umbraco/Surface/Post/GetStockComment?symbol=")
public interface AtaYatirimFinanceService {

    @GetMapping
    @Retry(name = "finance")
    @CircuitBreaker(name = "finance", fallbackMethod = "getBISTInformationFallback")
    AtaYatirimFinanceApiDTO getBISTInformation(@RequestParam String symbol);

    default AtaYatirimFinanceApiDTO getBISTInformationFallback(String endeks, Throwable t) {
        return new AtaYatirimFinanceApiDTO();
    }
}
