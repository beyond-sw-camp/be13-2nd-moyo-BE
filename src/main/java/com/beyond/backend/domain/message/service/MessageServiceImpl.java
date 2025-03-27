package com.beyond.backend.domain.message.service;

import com.beyond.backend.domain.common.dto.RequestNotificationDto;
import com.beyond.backend.domain.common.entity.NotificationType;
import com.beyond.backend.domain.common.entity.UserStatus;
import com.beyond.backend.domain.common.exception.BaseException;
import com.beyond.backend.domain.common.exception.UserException;
import com.beyond.backend.domain.common.exception.message.ExceptionMessage;
import com.beyond.backend.domain.common.service.NotificationService;
import com.beyond.backend.domain.message.dto.MessageDto;
import com.beyond.backend.domain.message.dto.MessageResponseDto;
import com.beyond.backend.domain.message.entity.Message;
import com.beyond.backend.domain.message.repository.MessageRepository;
import com.beyond.backend.domain.user.dto.CustomUserDetails;
import com.beyond.backend.domain.user.entity.User;
import com.beyond.backend.domain.user.repository.UserRepository;
import com.beyond.backend.domain.user.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.beyond.backend.domain.message.dto.MessageResponseDto.returnMessageDto;

/**
 * <p>
 * <p>packageName    : com.beyond.backend.service.impl
 * <p>fileName       : MessageServiceImpl
 * <p>author         : mlnstone
 * <p>date           : 2025. 2. 16.
 * <p>description    :
 */
/*
===========================================================
DATE              AUTHOR             NOTE
-----------------------------------------------------------
2025. 2. 16.        mlnstone             최초 생성
*/
@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {
    private final UserRepository userRepository;
    private final MessageRepository messageRepository;
    private final NotificationService notificationService;
    private final AuthService authService;

    @Override
    @Transactional //안읽음때메 붙임
    public MessageResponseDto getMessage(Long userNo, Long messageNo) {
        Message message = messageRepository.findById(messageNo)
                .filter(m -> m.hasPermission(userNo))
                .orElseThrow(() -> new RuntimeException("확인할 수 없는 쪽지입니다."));
        boolean isSender = message.getSender() != null && message.getSender().getNo().equals(userNo);
        boolean isReceiver = message.getReceiver() != null && message.getReceiver().getNo().equals(userNo);
        if (isSender && message.isDeletedBySender()) {
            throw new BaseException(ExceptionMessage.MESSAGE_NOT_FOUND, "확인할 수 없는 쪽지입니다.");
        }
        if (isReceiver && message.isDeletedByReceiver()) {
            throw new BaseException(ExceptionMessage.MESSAGE_NOT_FOUND, "확인할 수 없는 쪽지입니다.");

        }
        if (message.getReceiver() != null && message.getReceiver().getNo().equals(userNo)) {
            message.markAsRead(); // 읽음 처리
        }
        return returnMessageDto(message);
    }

    @Override
    @Transactional
    public MessageResponseDto messageWrite(Long userNo, MessageDto messageDto) {

        User sender = userRepository.findById(userNo).orElseThrow(() -> new IllegalArgumentException("User Not Found: " + userNo));

        User receiver = userRepository.findByUsername(messageDto.getReceiverUsername())
                .filter(user -> user.getUserStatus() != UserStatus.INACTIVE)
                .orElseThrow(() -> new IllegalArgumentException("받는 회원이 존재하지 않거나 비활성화된 회원입니다."));

        Message message = Message.builder()
                .sender(sender)
                .receiver(receiver)
                .content(messageDto.getContent())
                .build();

        if (!sender.equals(receiver)) {


            // 트랜잭션 종료 후가 아니라, 바로 알림 전송
            notificationService.sendNotification(
                    new RequestNotificationDto(
                            sender.getUsername(),
                            receiver.getUsername(),
                            NotificationType.MESSAGE,
                            sender.getUsername() + "님의 쪽지가 도착했습니다.")
            );
        }

        messageRepository.save(message);
        return returnMessageDto(message);

    }

    @Override
    public Page<MessageResponseDto> getMessageList(Long userNo, String type, Pageable pageable) {

        Page<Message> messages;


        if ("sent".equalsIgnoreCase(type)) {
            messages = messageRepository.findAllBySender_NoAndDeletedBySenderFalse(userNo, pageable);
        } else {
            messages = messageRepository.findAllByReceiver_NoAndDeletedByReceiverFalse(userNo, pageable);
        }

        return messages.map(MessageResponseDto::returnMessageDto);
    }

    @Override
    @Transactional
    public Object deleteMessage(Long userNo, Long messageNo) {

        Message message = messageRepository.findById(messageNo).orElseThrow(()
                -> new BaseException(ExceptionMessage.MESSAGE_NOT_FOUND));

        boolean isSender = message.getSender() != null && message.getSender().getNo().equals(userNo);
        boolean isReceiver = message.getReceiver() != null && message.getReceiver().getNo().equals(userNo);
        if (!(isSender || isReceiver)) {
            throw new UserException(ExceptionMessage.USER_ACCESS_DENIED, "유저 정보가 일치하지 않습니다.");
        }

        if (isSender) {
            message.deleteBySender();
        }
        if (isReceiver) {
            message.deleteByReceiver();
        }
        // sender가 null이고 receiver가 삭제되면 하드 딜리트
        if (message.getSender() == null && message.isDeletedByReceiver()) {
            messageRepository.delete(message);
            return "메시지 삭제";
        }

        // receiver가 null이고 sender가 삭제되면 하드 딜리트
        if (message.getReceiver() == null && message.isDeletedBySender()) {
            messageRepository.delete(message);
            return "메시지 삭제";
        }
        if (message.isDeleted()) {
            messageRepository.delete(message);
            return "양쪽 모두 삭제";
        }
        return "";

    }

    @Override
    public long getUnreadMessageCount(Long userNo) { // 안읽음 개수
        return messageRepository.countByReceiverNoAndIsReadFalse(userNo);
    }


}
