package com.yapp.project.notification.controller;

import com.yapp.project.aux.Message;
import com.yapp.project.aux.common.AccountUtil;
import com.yapp.project.notification.domain.dto.NotificationResponse;
import com.yapp.project.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/notification")
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping("/")
    public NotificationResponse getNotificationList() {
        return notificationService.getNotificationList(AccountUtil.getAccount());
    }

    @PatchMapping("/{id}")
    public Message readNotification(@PathVariable Long id) {
        return notificationService.readNotification(AccountUtil.getAccount(), id);
    }

}
