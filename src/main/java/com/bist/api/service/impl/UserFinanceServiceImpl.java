package com.bist.api.service.impl;

import com.bist.api.repository.UserFinanceRepository;
import com.bist.api.rest.dto.UserFinanceHistoryDTO;
import com.bist.api.service.UserFinanceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;


@Slf4j
@Service
@RequiredArgsConstructor
public class UserFinanceServiceImpl implements UserFinanceService {

    private final UserFinanceRepository userFinanceRepository;

    @Override
    public UserFinanceHistoryDTO getUserFinance(String username) {
        var ifNotExist = UserFinanceHistoryDTO.builder().history(new ArrayList<>()).build();
        var userFinanceEntity = userFinanceRepository.findById(username);
        return userFinanceEntity.map(userFinance -> UserFinanceHistoryDTO.builder().history(userFinance.getHistory()).build()).orElse(ifNotExist);
    }
}
