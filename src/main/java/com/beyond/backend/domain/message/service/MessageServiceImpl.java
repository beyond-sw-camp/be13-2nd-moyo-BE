package com.beyond.backend.domain.message.service;

import com.beyond.backend.domain.common.dto.RequestNotificationDto;
import com.beyond.backend.domain.common.entity.NotificationType;
import com.beyond.backend.domain.common.entity.UserStatus;
import com.beyond.backend.domain.common.service.NotificationService;
import com.beyond.backend.domain.message.dto.MessageDto;
import com.beyond.backend.domain.message.dto.MessageResponseDto;
import com.beyond.backend.domain.message.entity.Message;
import com.beyond.backend.domain.message.repository.MessageRepository;
import com.beyond.backend.domain.user.entity.User;
import com.beyond.backend.domain.user.repository.UserRepository;
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

    @Override
    @Transactional //안읽음때메 붙임
    public MessageResponseDto getMessage(Long userNo, Long messageNo) {

        Message message = messageRepository.findById(messageNo)
                .filter(m -> m.hasPermission(userNo))
                .orElseThrow(() -> new RuntimeException("확인할 수 없는 쪽지입니다."));
        boolean isSender = message.getSender() != null && message.getSender().getNo().equals(userNo);
        boolean isReceiver = message.getReceiver() != null && message.getReceiver().getNo().equals(userNo);
        if (isSender && message.isDeletedBySender()) {
            throw new RuntimeException("확인할 수 없는 쪽지입니다.");
        }
        if (isReceiver && message.isDeletedByReceiver()) {
            throw new RuntimeException("확인할 수 없는 쪽지입니다.");
        }
        if (message.getReceiver() != null && message.getReceiver().getNo().equals(userNo)) {
            message.markAsRead(); // 읽음 처리
        }
        return returnMessageDto(message);
    }

    @Override
    @Transactional
    public MessageResponseDto messageWrite(User sender, MessageDto messageDto) {

        User receiver = userRepository.findByUsername(messageDto.getReceiverId())
                .filter(user -> user.getUserStatus() != UserStatus.INACTIVE)
                .orElseThrow(() -> new IllegalArgumentException("받는 회원이 존재하지 않거나 비활성화된 회원입니다."));

        Message message = Message.builder()
                .sender(sender)
                .receiver(receiver)
                .content(messageDto.getContent())
                .deletedBySender(false)
                .deletedByReceiver(false)
                .build();


        // 트랜잭션 종료 후가 아니라, 바로 알림 전송
        notificationService.sendNotification(
                new RequestNotificationDto(
                        sender.getUsername(),
                        receiver.getUsername(),
                        NotificationType.MESSAGE,
                        sender.getUsername() + "님의 쪽지가 도착됨" + message.getContent())
        );

        messageRepository.save(message);

        return returnMessageDto(message);

    }

    @Override // 얘도 userNo 받을 때 인증과정한다음에  하기
    public Page<MessageResponseDto> getReceivedMessageList(Long userNo, Pageable pageable) {
        Page<Message> receivedMessages = messageRepository.findAllByReceiver_NoAndDeletedByReceiverFalse(userNo, pageable);

        return receivedMessages.map(MessageResponseDto::returnMessageDto);
    }

    @Override // 얘도 userNo 받을 때 인증과정한다음에 하기
    public Page<MessageResponseDto> getSentMessageList(Long userNo, Pageable pageable) {
        Page<Message> sentMessages = messageRepository.findAllBySender_NoAndDeletedBySenderFalse(userNo, pageable);

        return sentMessages.map(MessageResponseDto::returnMessageDto);
    }

    @Override
    @Transactional
    public Object deleteMessage(Long userNo, Long messageNo) {
        Message message = messageRepository.findById(messageNo).orElseThrow(()
                -> new IllegalArgumentException("존재하지 않는 메시지입니다."));

        boolean isSender = message.getSender() != null && message.getSender().getNo().equals(userNo);
        boolean isReceiver = message.getReceiver() != null && message.getReceiver().getNo().equals(userNo);
        if (!(isSender || isReceiver)) {
            throw new IllegalArgumentException("유저 정보가 일치하지 않습니다.");
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

    public long getUnreadMessageCount(Long userNo) { // 안읽음 개수
        return messageRepository.countByReceiverNoAndIsReadFalse(userNo);
    }


}
