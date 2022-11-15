package com.bist.api.service;

import com.bist.api.rest.dto.IsYatirimFinanceApiDTO;
import com.bist.api.rest.dto.AtaYatirimFinanceApiDTO;

import java.util.List;

public interface ProcessData {
    void processData(List<IsYatirimFinanceApiDTO> bistShare);

    void processDataForATA(AtaYatirimFinanceApiDTO ataYatirimFinanceApiDTO, String bist);

}
