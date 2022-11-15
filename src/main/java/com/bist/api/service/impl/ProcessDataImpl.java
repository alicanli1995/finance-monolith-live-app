package com.bist.api.service.impl;

import com.bist.api.model.BistModel;
import com.bist.api.model.Share;
import com.bist.api.repository.BistValueRepository;
import com.bist.api.repository.BistsRepository;
import com.bist.api.rest.dto.AtaYatirimFinanceApiDTO;
import com.bist.api.rest.dto.IsYatirimFinanceApiDTO;
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
    public void processData(List<IsYatirimFinanceApiDTO> bistShare) {
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

    @Override
    public void processDataForATA(AtaYatirimFinanceApiDTO ataYatirimFinanceApiDTO, String bist) {
        bistValueRepository.save(BistModel.builder()
                .id(UUID.randomUUID().toString())
                .name(bist)
                .value(ataYatirimFinanceApiDTO.getResult().getSonFiyat())
                .createdAt(LocalDateTime.now())
                .build());
        updateBistPriceForAta(ataYatirimFinanceApiDTO,bist);
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
