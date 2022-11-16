package com.bist.api.service;

import com.bist.api.rest.dto.BistNotificationRequestDTO;

public interface NotificationService {

    void notifyBist(BistNotificationRequestDTO bistNotificationRequestDTO);

    boolean isNotified(String bistName, String mailAddress);

}
