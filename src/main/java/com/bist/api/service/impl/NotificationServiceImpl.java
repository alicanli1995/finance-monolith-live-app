package com.bist.api.service.impl;

import com.bist.api.model.BistNotification;
import com.bist.api.repository.BistNotificationRepository;
import com.bist.api.rest.dto.BistNotificationRequestDTO;
import com.bist.api.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final BistNotificationRepository bistNotificationRepository;

    @Override
    public void notifyBist(BistNotificationRequestDTO bistNotificationRequestDTO) {
        bistNotificationRepository.findByBistNameAndMailAddress(bistNotificationRequestDTO.getBistName(), bistNotificationRequestDTO.getMail())
                .ifPresentOrElse(bistNotification -> log.error("Bist notification already exists"), () -> bistNotificationRepository.save(BistNotification.builder()
                        .bistName(bistNotificationRequestDTO.getBistName())
                        .mailAddress(bistNotificationRequestDTO.getMail())
                        .value(bistNotificationRequestDTO.getValue())
                        .isSent(false)
                        .build()));
    }

    @Override
    public boolean isNotified(String bistName, String mailAddress) {
        Optional<BistNotification> byBistNameAndMailAddress = bistNotificationRepository.findByBistNameAndMailAddress(bistName, mailAddress);
        return byBistNameAndMailAddress.filter(bistNotification -> !bistNotification.isSent()).isPresent();
    }
}
