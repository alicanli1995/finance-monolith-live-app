package com.bist.api.scheduler;

import com.bist.api.CommonConstants;
import com.bist.api.client.AtaYatirimFinanceService;
import com.bist.api.client.IsYatirimFinanceService;
import com.bist.api.config.FinanceDataStreamConfig;
import com.bist.api.rest.dto.AtaYatirimFinanceApiDTO;
import com.bist.api.rest.dto.IsYatirimFinanceApiDTO;
import com.bist.api.service.ProcessData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class FinanceStreamApi {

    private final IsYatirimFinanceService isYatirimFinanceService;
    private final AtaYatirimFinanceService ataYatirimFinanceService;
    private final FinanceDataStreamConfig financeDataStreamConfig;
    private final ProcessData processData;

    @Value("${finance.resource}")
    private String financeResource;



   @Scheduled( cron = "1 0/15 10-18 ? * MON-FRI",
               zone = "Europe/Istanbul")
//    @Scheduled(fixedDelay = 1000000)
    private void getBistInformation() {
        financeDataStreamConfig
                .getShareList()
                .forEach(bist ->
                {
                    if (financeResource.equals(CommonConstants.IS_YATIRIM)){
                        var endeksUrl = bist.concat(financeDataStreamConfig.getUrlAppend());
                        List<IsYatirimFinanceApiDTO> bistShare = new ArrayList<>();
                        try {
                            bistShare = isYatirimFinanceService.getBISTInformation(endeksUrl);
                            processData.processData(bistShare.get(0),bist);
                            Thread.sleep(8000);
                            log.info("Bist information is processed {}", bistShare);
                        } catch (InterruptedException e) {
                            log.error("Bist information is not processed {}", bistShare);
                            throw new RuntimeException(e);
                        }
                    }else {
                        AtaYatirimFinanceApiDTO ataYatirimFinanceApiDTO = null;
                        try {
                            ataYatirimFinanceApiDTO = ataYatirimFinanceService.getBISTInformation(bist);
                            log.info("Bist information is processed {}", ataYatirimFinanceApiDTO);
                            processData.processData(ataYatirimFinanceApiDTO, bist);
                            Thread.sleep(10000);
                        } catch (Exception e) {
                            log.error("Bist information is not processed {}", ataYatirimFinanceApiDTO);
                            throw new RuntimeException(e);
                        }
                    }

                });

    }


}
