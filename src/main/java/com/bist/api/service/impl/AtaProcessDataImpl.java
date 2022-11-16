package com.bist.api.service.impl;

import com.bist.api.model.BistModel;
import com.bist.api.model.Share;
import com.bist.api.repository.BistValueRepository;
import com.bist.api.repository.BistsRepository;
import com.bist.api.rest.dto.AtaYatirimFinanceApiDTO;
import com.bist.api.service.ProcessData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "finance-share", name = "resource", havingValue = "ata")
public class AtaProcessDataImpl implements ProcessData {

    private final BistValueRepository bistValueRepository;

    private final BistsRepository bistsRepository;


    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void processData(Object ataYatirimFinanceApiDTO, String bist) {
        if(ataYatirimFinanceApiDTO instanceof AtaYatirimFinanceApiDTO ataYatirimFinanceApiDTO1) {
            bistValueRepository.save(BistModel.builder()
                    .id(UUID.randomUUID().toString())
                    .name(bist)
                    .value(ataYatirimFinanceApiDTO1.getResult().getSonFiyat())
                    .createdAt(LocalDateTime.now())
                    .build());
        updateBistPriceForAta(ataYatirimFinanceApiDTO1,bist);
        }
    }

    private void updateBistPriceForAta(AtaYatirimFinanceApiDTO ataYatirimFinanceApiDTO, String bist) {
        if (bistsRepository.existsById(bist)) {
            Share shareFromDb = bistsRepository.findById(bist).get();
            shareFromDb.setValue(ataYatirimFinanceApiDTO.getResult().getSonFiyat());
            shareFromDb.setDailyChange(ataYatirimFinanceApiDTO.getResult().getFark());
            shareFromDb.setDailyChangePercentage(ataYatirimFinanceApiDTO.getResult().getFarkYuzde());
            shareFromDb.setPreviousDayClose(ataYatirimFinanceApiDTO.getResult().getOncGun());
            shareFromDb.setDailyVolume(ataYatirimFinanceApiDTO.getResult().getHacim());
            bistsRepository.save(shareFromDb);
        } else {
            Share shareToSave = Share.builder()
                    .dailyVolume(ataYatirimFinanceApiDTO.getResult().getHacim())
                    .dailyChange(ataYatirimFinanceApiDTO.getResult().getFark())
                    .previousDayClose(ataYatirimFinanceApiDTO.getResult().getOncGun())
                    .name(bist)
                    .description(ataYatirimFinanceApiDTO.getResult().getTanim())
                    .value(ataYatirimFinanceApiDTO.getResult().getSonFiyat())
                    .dailyChangePercentage(ataYatirimFinanceApiDTO.getResult().getFarkYuzde())
                    .build();
            bistsRepository.save(shareToSave);
        }
    }

}
