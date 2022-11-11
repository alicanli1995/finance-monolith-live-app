package com.bist.api.scheduler;

import com.bist.api.client.FinanceService;
import com.bist.api.config.FinanceDataStreamConfig;
import com.bist.api.rest.dto.FinanceApiDTO;
import com.bist.api.service.ProcessData;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class FinanceStreamApi {

    private final FinanceService financeService;
    private final FinanceDataStreamConfig financeDataStreamConfig;
    private final ProcessData processData;


   @Scheduled( cron = "1 0/15 10-18 ? * MON-FRI",
               zone = "Europe/Istanbul")
//    @Scheduled(fixedDelay = 1000000)
    private void getBistInformation() {
        financeDataStreamConfig
                .getShareList()
                .forEach(bist ->
                {
                    var endeksUrl = bist.concat(financeDataStreamConfig.getUrlAppend());
                    List<FinanceApiDTO> bistShare = new ArrayList<>();
                    try {
                        bistShare = financeService.getBISTInformation(endeksUrl);
                        processData.processData(bistShare);
                        Thread.sleep(8000);
                        log.info("Bist information is processed {}", bistShare);
                    } catch (InterruptedException e) {
                        log.error("Bist information is not processed {}", bistShare);
                        throw new RuntimeException(e);
                    }
                });

    }


}
