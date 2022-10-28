package com.bist.api.service;

import com.bist.api.rest.dto.UserFinanceHistoryDTO;

public interface UserFinanceService {
    UserFinanceHistoryDTO getUserFinance(String username);
}
