package com.bist.api.service.impl;

import com.bist.api.model.BistModel;
import com.bist.api.model.Share;
import com.bist.api.repository.BistValueRepository;
import com.bist.api.repository.BistsRepository;
import com.bist.api.rest.dto.IsYatirimFinanceApiDTO;
import com.bist.api.service.ProcessData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "finance-share", name = "resource", havingValue = "is")
public class ISProcessDataImpl implements ProcessData {

    private final BistValueRepository bistValueRepository;

    private final BistsRepository bistsRepository;
    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void processData(Object bistShare,String bist) {
        if (bistShare instanceof IsYatirimFinanceApiDTO isYatirimFinanceApiDTO) {
                if (new BigDecimal(isYatirimFinanceApiDTO.last()).compareTo(BigDecimal.ZERO) == 0) return;
                bistValueRepository.save(BistModel.builder()
                        .id(UUID.randomUUID().toString())
                        .name(bist)
                        .value(isYatirimFinanceApiDTO.last())
                        .createdAt(LocalDateTime.now())
                        .build());
                updateBistPrice(isYatirimFinanceApiDTO, bist);
        }
    }


    private void updateBistPrice(IsYatirimFinanceApiDTO isYatirimFinanceApiDTO, String name) {
            if (bistsRepository.existsById(name)) {
                Share shareFromDb = bistsRepository.findById(name).get();
                shareFromDb.setValue(isYatirimFinanceApiDTO.last());
                shareFromDb.setDailyChange(isYatirimFinanceApiDTO.dailyChange());
                shareFromDb.setDailyChangePercentage(isYatirimFinanceApiDTO.dailyChangePercentage());
                shareFromDb.setPreviousDayClose(isYatirimFinanceApiDTO.previousDayClose());
                shareFromDb.setDailyVolume(isYatirimFinanceApiDTO.dailyVolume());
                bistsRepository.save(shareFromDb);
            } else {
                Share shareToSave = Share.builder()
                        .dailyVolume(isYatirimFinanceApiDTO.dailyVolume())
                        .dailyChange(isYatirimFinanceApiDTO.dailyChange())
                        .previousDayClose(isYatirimFinanceApiDTO.previousDayClose())
                        .name(name)
                        .description(isYatirimFinanceApiDTO.description())
                        .value(isYatirimFinanceApiDTO.last())
                        .dailyChangePercentage(isYatirimFinanceApiDTO.dailyChangePercentage())
                        .build();
                bistsRepository.save(shareToSave);
            }
    }
}
