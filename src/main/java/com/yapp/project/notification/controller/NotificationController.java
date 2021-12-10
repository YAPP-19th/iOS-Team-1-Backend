package com.yapp.project.notification.controller;

import com.yapp.project.aux.Message;
import com.yapp.project.aux.common.AccountUtil;
import com.yapp.project.notification.domain.dto.NotificationResponse;
import com.yapp.project.notification.service.NotificationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/notification")
@Api(tags = "알림 내역")
public class NotificationController {

    private final NotificationService notificationService;

    @ApiOperation(value = "알림 내역 전체 조회", notes = "알림 내역 전체 조회하기")
    @GetMapping("/")
    public NotificationResponse getNotificationList() {
        return notificationService.getNotificationList(AccountUtil.getAccount());
    }

    @ApiOperation(value = "알림 내역 읽음 처리", notes = "앍림 내역 읽음 처리하기")
    @PatchMapping("/{id}")
    public Message readNotification(@PathVariable Long id) {
        return notificationService.readNotification(AccountUtil.getAccount(), id);
    }

}
