package com.bist.api.scheduler;

import com.bist.api.model.BistNotification;
import com.bist.api.repository.BistNotificationRepository;
import com.bist.api.repository.BistsRepository;
import com.bist.api.service.MailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
@RequiredArgsConstructor
public class SendNotifyScheduler {

    private final MailService mailService;
    private final BistsRepository bistsRepository;
    private final BistNotificationRepository bistNotificationRepository;

    private ConcurrentHashMap<String, BigDecimal> bists = new ConcurrentHashMap<>();

    @Scheduled( cron = "1 0/20 10-18 ? * MON-FRI",
            zone = "Europe/Istanbul")
//    @Scheduled(fixedDelay = 10000)
    public void sendNotify() {
        bistsRepository.findAll().forEach(bist -> bists.put(
                bist.getName(),
                new BigDecimal(bist.getValue())));

        log.info("SendNotifyScheduler started");
        List<BistNotification> bistNotifications = bistNotificationRepository.findAll()
                .stream()
                .filter(bistNotification -> !bistNotification.isSent()).toList();

        bistNotifications.forEach(bistNotification -> {
            if (bists.get(bistNotification.getBistName())
                    .compareTo(new BigDecimal(bistNotification.getValue())) > 0) {
                var message = "Bist " + bistNotification.getBistName() + " value is " +
                        bists.get(bistNotification.getBistName()) + " and it's over "+ bistNotification.getValue() + " , congratulations!";
                mailService.sendMail(bistNotification.getMailAddress(), "Bist Notification", message);
                bistNotification.setSent(true);
                bistNotificationRepository.save(bistNotification);
            }
        });
    }



}
