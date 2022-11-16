package com.bist.api.rest;


import com.bist.api.rest.dto.BistNotificationRequestDTO;
import com.bist.api.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.RequestScope;


@Slf4j
@RequestScope
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    @PostMapping(produces = "application/json")
    public void notifyBist(@RequestBody BistNotificationRequestDTO bistNotificationRequestDTO) {
        notificationService.notifyBist(bistNotificationRequestDTO);
    }

    @GetMapping(produces = "application/json")
    public boolean isNotified(@RequestParam String bistName, @RequestParam String mailAddress) {
        return notificationService.isNotified(bistName, mailAddress);
    }


}
