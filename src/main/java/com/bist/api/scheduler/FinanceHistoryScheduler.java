package com.bist.api.scheduler;

import com.bist.api.model.UserFinanceHistory;
import com.bist.api.repository.UserFinanceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;


@Slf4j
@Component
@RequiredArgsConstructor
public class FinanceHistoryScheduler {

    private final UserFinanceRepository userFinanceRepository;

    @Scheduled(cron = "0 0 22 * * MON-FRI",
            zone = "Europe/Istanbul")
    public void deleteFinanceHistoryIfPassed15Days(){
        userFinanceRepository.findAll().forEach(userFinance -> {
            userFinance.getHistory().removeIf(this::isPassed15Days);
            userFinanceRepository.save(userFinance);
        });
    }

    private boolean isPassed15Days(UserFinanceHistory.History user) {
        log.warn(user.getDate().isBefore(LocalDateTime.now()) ? "Record is deleted : " + user.getDate() : "");
        return user.getDate().isBefore(LocalDateTime.now().minusDays(15));
    }


}
