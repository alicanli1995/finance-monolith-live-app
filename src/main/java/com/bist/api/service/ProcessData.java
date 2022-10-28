package com.bist.api.service;

import com.bist.api.rest.dto.FinanceApiDTO;

import java.util.List;

public interface ProcessData {
    void processData(List<FinanceApiDTO> bistShare);

}
