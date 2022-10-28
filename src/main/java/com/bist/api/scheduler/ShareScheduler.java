package com.bist.api.scheduler;

import com.bist.api.repository.BistValueRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

@Slf4j
@Component
@RequiredArgsConstructor
public class ShareScheduler {

    private final BistValueRepository bistValueRepository;

    @Scheduled(cron = "0 0 20 * * MON-FRI",
            zone = "Europe/Istanbul")
//    @Scheduled(fixedDelay = 1000000)
    private void deleteShareIfNotInShiftTime() {
        bistValueRepository.findAll()
                    .stream()
                    .parallel()
                    .filter(share -> isNotInShiftTime(share.getCreatedAt()) || shareIsZero(share.getValue()))
                    .peek(share -> log.info("Share {} will be deleted", share.getName()))
                    .forEach(share -> bistValueRepository.deleteById(share.getId()));
    }

    private boolean shareIsZero(String value) {
        return value.equals("0.0");
    }

    private boolean isNotInShiftTime(LocalDateTime createdAt) {
        return
                isShiftTime(createdAt) ||
                isHolidayOrNot(Date.from(createdAt.atZone(ZoneId.systemDefault()).toInstant())) ||
                isShareRecordPastFiveDays(createdAt);
    }


    private boolean isShiftTime(LocalDateTime createdAt) {
        var hour = createdAt.getHour();
        if (hour < 10 || hour >= 18) {
            if (hour == 18 && createdAt.getMinute() <= 35) {
                return Boolean.FALSE;
            }
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    private boolean isShareRecordPastFiveDays(LocalDateTime createdAt) {
        return createdAt.isBefore(LocalDateTime.now().minusDays(5));
    }


    private boolean isHolidayOrNot(Date d) {
        Calendar c = new GregorianCalendar();
        c.setTime(d);
        return (Calendar.SATURDAY == c.get(Calendar.DAY_OF_WEEK)) || (Calendar.SUNDAY == c.get(Calendar.DAY_OF_WEEK));
    }


}
