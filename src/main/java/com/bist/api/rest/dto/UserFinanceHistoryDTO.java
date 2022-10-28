package com.bist.api.rest.dto;

import com.bist.api.model.UserFinanceHistory;
import lombok.Builder;

import java.util.List;

@Builder
public record UserFinanceHistoryDTO(List<UserFinanceHistory.History> history) {
}
