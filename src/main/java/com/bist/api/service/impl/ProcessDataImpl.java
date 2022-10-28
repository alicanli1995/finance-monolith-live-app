package com.bist.api.service.impl;

import com.bist.api.model.BistModel;
import com.bist.api.model.Share;
import com.bist.api.repository.BistValueRepository;
import com.bist.api.repository.BistsRepository;
import com.bist.api.rest.dto.FinanceApiDTO;
import com.bist.api.service.ProcessData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProcessDataImpl implements ProcessData {

    private final BistValueRepository bistValueRepository;

    private final BistsRepository bistsRepository;
    @Override
    @Transactional
    public void processData(List<FinanceApiDTO> bistShare) {
        String bistName = bistShare.get(0).c().trim().split("\\.")[0];
        bistShare
                .forEach(
                share -> {
                    if(new BigDecimal(share.last()).compareTo(BigDecimal.ZERO) == 0) return;
                    bistValueRepository.save(BistModel.builder()
                            .id(UUID.randomUUID().toString())
                            .name(bistName)
                            .value(share.last())
                            .createdAt(LocalDateTime.now())
                            .build());
                    updateBistPrice(bistShare.get(0),bistName);
                }
        );
    }

    private void updateBistPrice(FinanceApiDTO financeApiDTO, String name) {
            if (bistsRepository.existsById(name)) {
                Share shareFromDb = bistsRepository.findById(name).get();
                shareFromDb.setValue(financeApiDTO.last());
                shareFromDb.setDailyChange(financeApiDTO.dailyChange());
                shareFromDb.setDailyChangePercentage(financeApiDTO.dailyChangePercentage());
                shareFromDb.setPreviousDayClose(financeApiDTO.previousDayClose());
                shareFromDb.setDailyVolume(financeApiDTO.dailyVolume());
                bistsRepository.save(shareFromDb);
            } else {
                Share shareToSave = Share.builder()
                        .dailyVolume(financeApiDTO.dailyVolume())
                        .dailyChange(financeApiDTO.dailyChange())
                        .previousDayClose(financeApiDTO.previousDayClose())
                        .name(name)
                        .description(financeApiDTO.description())
                        .value(financeApiDTO.last())
                        .dailyVolume(financeApiDTO.dailyVolume())
                        .dailyChangePercentage(financeApiDTO.dailyChangePercentage())
                        .build();
                bistsRepository.save(shareToSave);
            }
    }
}
