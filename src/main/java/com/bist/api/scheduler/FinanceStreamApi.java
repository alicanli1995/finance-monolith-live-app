package com.bist.api.scheduler;

import com.bist.api.client.FinanceService;
import com.bist.api.config.FinanceDataStreamConfig;
import com.bist.api.service.ProcessData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class FinanceStreamApi {

    private final FinanceService financeService;
    private final FinanceDataStreamConfig financeDataStreamConfig;
    private final ProcessData processData;


//    @Scheduled( cron = "1 0/15 10-18 ? * MON-FRI",
//                zone = "Europe/Istanbul")
//    @Scheduled(fixedDelay = 1000000)
    private void getBistInformation() {
        financeDataStreamConfig
                .getShareList()
                .forEach(bist ->
                {
                    var endeksUrl = bist.concat(financeDataStreamConfig.getUrlAppend());
                    var bistShare = financeService.getBISTInformation(endeksUrl);
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    if (bistShare.isEmpty()) throw new RuntimeException("Error on getting information... Try later !");
                    processData.processData(bistShare);
                    log.info("Bist information is processed");

                });

    }


}
