package com.yapp.project.notification.service;

import com.yapp.project.account.domain.Account;
import com.yapp.project.aux.Message;
import com.yapp.project.aux.StatusEnum;
import com.yapp.project.aux.common.DateUtil;
import com.yapp.project.aux.content.NotificationContent;
import com.yapp.project.config.exception.notification.NotificationNotFoundException;
import com.yapp.project.notification.domain.Notification;
import com.yapp.project.notification.domain.NotificationRepository;
import com.yapp.project.notification.domain.dto.NotificationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;

    @Transactional(readOnly = true)
    public NotificationResponse getNotificationList(Account account) {
        LocalDate lastMonth = DateUtil.KST_LOCAL_DATE_NOW().minusMonths(1);
        List<Notification> notificationList = notificationRepository.findAllByAccountAndDateIsAfterOrderByDateDesc(account, lastMonth);
        return NotificationResponse.builder()
                .data(notificationList)
                .message(Message.of(StatusEnum.NOTIFICATION_OK, NotificationContent.NOTIFICATION_LIST_GET_OK)).build();
    }

    @Transactional
    public Message readNotification(Account account, Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(NotificationNotFoundException::new);
        checkIsMine(account, notification);
        notification.updateRead();
        notificationRepository.save(notification);
        return Message.builder()
                .msg(NotificationContent.NOTIFICATION_READ_OK)
                .status(StatusEnum.NOTIFICATION_OK).build();
    }

    private void checkIsMine(Account account, Notification notification) {
        if(account.equals(notification.getAccount())) {
            throw new NotificationNotFoundException();
        }
    }

    @Transactional
    public void createNotification(Account account, String title, String content) {
        Notification notification = Notification.builder().account(account).title(title).content(content).build();
        notificationRepository.save(notification);
    }

}
