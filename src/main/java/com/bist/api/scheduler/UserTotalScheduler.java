package com.bist.api.scheduler;

import com.bist.api.model.UserFinanceHistory;
import com.bist.api.repository.BistsRepository;
import com.bist.api.repository.UserFinanceRepository;
import com.bist.api.repository.UserShareRepository;
import com.bist.api.rest.dto.UserSharesDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserTotalScheduler {

    private final UserFinanceRepository userFinanceRepository;
    private final BistsRepository bistsRepository;
    private final UserShareRepository userShareRepository;
    private ConcurrentHashMap<String, BigDecimal> bistsMap = new ConcurrentHashMap<>();


    @Scheduled(cron = "0 0 21 * * MON-FRI",
            zone = "Europe/Istanbul")
    private void updateUserFinanceHistory() {

        log.info("User Finance History Update Started");

        bistsRepository.findAll().forEach(bist -> bistsMap.put(bist.getName(), new BigDecimal(bist.getValue())));

        userShareRepository.findAll()
                .stream()
                .parallel()
                .forEach(
                userShares -> userFinanceRepository.findById(userShares.getUsername()).orElseGet(() -> {
                    var userFinanceHistory = UserFinanceHistory.builder()
                            .username(userShares.getUsername())
                            .history(new ArrayList<>())
                            .build();
                    return userFinanceRepository.save(userFinanceHistory);
                })
        );

        userFinanceRepository.findAll().forEach(userFinance -> {
            userFinance.getHistory().add(
                    UserFinanceHistory.History.builder()
                            .date(LocalDateTime.now())
                            .totalDailyValue(calcUserDailyValue(userFinance))
                            .totalDailyChange(calcUserDailyChange(userFinance))
                            .totalDailyChangePercentage(calcUserDailyChangePercentage(userFinance, calcUserDailyValue(userFinance)))
                            .build()
            );
            userFinanceRepository.save(userFinance);
        });

        log.info("User Finance History Update Finished");
    }

    private BigDecimal calcUserDailyChangePercentage(UserFinanceHistory userFinance, BigDecimal s) {
        UserFinanceHistory.History lastHistory;
        BigDecimal lastTotalValue ;
        BigDecimal todayChangeValue;

        if (Objects.nonNull(userFinance.getHistory()) && !userFinance.getHistory().isEmpty()){
            lastHistory = userFinance.getHistory().get(userFinance.getHistory().size() - 1);
            lastTotalValue = lastHistory.getTotalDailyValue();
            todayChangeValue = lastTotalValue.subtract(s);
        }
        else {
            lastTotalValue = BigDecimal.ZERO;
            todayChangeValue = BigDecimal.ZERO;
        }
        if (lastTotalValue.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        return todayChangeValue.divide(lastTotalValue, 4, RoundingMode.HALF_UP).negate();

    }

    private BigDecimal calcUserDailyChange(UserFinanceHistory userFinance) {
        if (Objects.nonNull(userFinance.getHistory()) && !userFinance.getHistory().isEmpty()){
            var lastHistory = userFinance.getHistory().get(userFinance.getHistory().size() - 1);
            var lastTotalValue = lastHistory.getTotalDailyValue();
            var currentTotalValue = calcUserDailyValue(userFinance);
            return lastTotalValue.subtract(currentTotalValue).negate();
        }
        else {
            var lastTotalValue = BigDecimal.ZERO;
            var currentTotalValue = calcUserDailyValue(userFinance);
            return lastTotalValue.subtract(currentTotalValue).negate();
        }

    }


    private BigDecimal calcUserDailyValue(UserFinanceHistory userFinance) {
        return userShareRepository.findById(userFinance.getUsername())
                .map(userShares -> userShares.getShares()
                        .stream()
                        .peek(userSharesDTO -> {
                            var bistInfo = bistsMap.get(userSharesDTO.getName());
                            userSharesDTO.setTotalAmount( calcDailyTotalAmount(String.valueOf(bistInfo), userSharesDTO.getAmount()));
                        })
                        .map(UserSharesDTO::getTotalAmount)
                        .map(BigDecimal::new)
                        .reduce(BigDecimal.ZERO, BigDecimal::add))
                .orElse(BigDecimal.ZERO);
    }

    private String calcDailyTotalAmount(String amount, String value) {
        return String.valueOf(new BigDecimal(amount).multiply(new BigDecimal(value)));
    }

    @Scheduled(cron = "0 0 22 * * MON-FRI",
            zone = "Europe/Istanbul")
    private void deleteIfWrongTime(){
        userFinanceRepository.findAll().forEach(userFinance -> {
            userFinance.getHistory().removeIf(history ->
                    isHolidayOrNot(Date.from(history.getDate().atZone(ZoneId.systemDefault()).toInstant()))
            );
            userFinanceRepository.save(userFinance);
        });
    }

    private boolean isHolidayOrNot(Date d) {
        Calendar c = new GregorianCalendar();
        c.setTime(d);
        return (Calendar.SATURDAY == c.get(Calendar.DAY_OF_WEEK)) || (Calendar.SUNDAY == c.get(Calendar.DAY_OF_WEEK));
    }

}
