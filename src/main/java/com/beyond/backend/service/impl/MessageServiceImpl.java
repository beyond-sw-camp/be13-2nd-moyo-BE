package com.beyond.backend.service.impl;

import com.beyond.backend.data.dto.MessageDto;
import com.beyond.backend.data.dto.MessageResponseDto;
import com.beyond.backend.data.entity.Message;
import com.beyond.backend.data.entity.User;
import com.beyond.backend.data.repository.MessageRepository;
import com.beyond.backend.data.repository.UserRepository;
import com.beyond.backend.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.beyond.backend.data.dto.MessageResponseDto.returnMessageDto;

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
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;

    @Override
    public MessageResponseDto getMessage(Long userNo, Long messageNo) {
        Message message = messageRepository.findById(messageNo)
                .orElseThrow(() -> new RuntimeException("메시지가 없습니다"));
        System.out.println("userNo " + userNo + " messageNo " + messageNo);
        if (!message.getReceiver().getNo().equals(userNo) &&
                !message.getSender().getNo().equals(userNo)) {
            System.out.println(message.getReceiver().getNo());
            System.out.println(message.getSender().getNo());
            throw new RuntimeException("해당 메시지를 조회할 권한이 없습니다.");
        } else if (message.getReceiver().getNo().equals(userNo)) {
            message.markAsRead(); // 읽음 처리
            messageRepository.save(message);
        }

        return returnMessageDto(message);
    }

    @Override
    @Transactional
    public MessageResponseDto messageWrite(MessageDto messageDto) {
        Optional<User> sender = userRepository.findByNo(messageDto.getSenderNo());
        Optional<User> receiver = userRepository.findByNo(messageDto.getReceiverNo());
        if (sender.isPresent() && receiver.isPresent()) {
            Message message = Message.builder()
                    .sender(sender.get())
                    .receiver(receiver.get())
                    .content(messageDto.getContent())
                    .sentAt(messageDto.getSendAt())
                    .deletedBySender(false)
                    .deletedByReceiver(false)
                    .build();
            messageRepository.save(message);

            return returnMessageDto(message);
        } else {

            throw new RuntimeException("존재하지 않는 유저입니다");
        }
    }

    private List<MessageResponseDto> getMessagesByUserId(Long userNo, boolean isSent, boolean isLatest) {
        List<Message> messages;

        if (isSent) {
            messages = isLatest ? messageRepository.findAllBySender_NoOrderByNoDesc(userNo)
                    : messageRepository.findAllBySender_NoOrderByNo(userNo);
        } else {
            messages = isLatest ? messageRepository.findAllByReceiver_NoOrderByNoDesc(userNo)
                    : messageRepository.findAllByReceiver_NoOrderByNo(userNo);
        }

        List<MessageResponseDto> messageResponseDto = new ArrayList<>();
        for (Message message : messages) {
            if (isSent && !message.isDeletedBySender() || !isSent && !message.isDeletedByReceiver()) {
                messageResponseDto.add(returnMessageDto(message));
            }
        }

        return messageResponseDto;
    }

    @Override
    public List<MessageResponseDto> getSentMessagesByOrder(Long userNo) {

        return getMessagesByUserId(userNo, true, false);
    }

    @Override
    public List<MessageResponseDto> getSentMessagesByLatest(Long userNo) {

        return getMessagesByUserId(userNo, true, true);
    }

    @Override
    public List<MessageResponseDto> getReceivedMessagesByOrder(Long userNo) {

        return getMessagesByUserId(userNo, false, false);
    }

    @Override
    public List<MessageResponseDto> getReceivedMessagesByLatest(Long userNo) {

        return getMessagesByUserId(userNo, false, true);
    }

    /* public List<MessageResponseDto> getSentMessagesByOrder(String userId) { // 시큐리티 되면 user 객체로
        List<Message> messages = messageRepository.findAllBySender_UserIdOrderByNo(userId);
        List<MessageResponseDto> messageResponseDto = new ArrayList<>();
        for (Message message : messages) {
            if (!message.isDeletedBySender()) {
                messageResponseDto.add(returnMessageDto(message));
            }
        }

        return messageResponseDto;
    }

    @Override
    public List<MessageResponseDto> getReceivedMessagesByOrder(String userId) { // 시큐리티 되면 user 객체로
        List<Message> messages = messageRepository.findAllByReceiver_UserIdOrderByNo(userId);
        List<MessageResponseDto> messageResponseDto = new ArrayList<>();
        for (Message message : messages) {
            if (!message.isDeletedByReceiver()) {
                messageResponseDto.add(returnMessageDto(message));
            }
        }
        return messageResponseDto;
    }

    @Override
    public List<MessageResponseDto> getSentMessagesByLatest(String userId) { // 시큐리티 되면 user 객체로
        List<Message> messages = messageRepository.findAllBySender_UserIdOrderByNoDesc(userId);
        List<MessageResponseDto> messageResponseDto = new ArrayList<>();
        for (Message message : messages) {
            if (!message.isDeletedBySender()) {
                messageResponseDto.add(returnMessageDto(message));
            }
        }
        return messageResponseDto;
    }

    @Override
    public List<MessageResponseDto> getReceivedMessagesByLatest(String userId) { // 시큐리티 되면 user 객체로
        List<Message> messages = messageRepository.findAllByReceiver_UserIdOrderByNoDesc(userId);
        List<MessageResponseDto> messageResponseDto = new ArrayList<>();
        for (Message message : messages) {
            if (!message.isDeletedByReceiver()) {
                messageResponseDto.add(returnMessageDto(message));
            }
        }
        return messageResponseDto;
    }


     */


    @Override
    @Transactional
    public Object deleteMessage(Long userNo, Long messageNo) {
        Message message = messageRepository.findById(messageNo).orElseThrow(()
                -> new IllegalArgumentException("존재하지 않는 메시지입니다."));

        boolean isSender = message.getSender().getNo().equals(userNo);
        boolean isReceiver = message.getReceiver().getNo().equals(userNo);

        if (isSender || isReceiver) {
            if (isSender) {
                message.deleteBySender();
            }
            if (isReceiver) {
                message.deleteByReceiver();
            }
            if (message.isDeleted()) {
                messageRepository.delete(message);
                return "양쪽 모두 삭제";
            }
            return "한쪽 삭제";
        } else {
            throw new IllegalArgumentException("유저 정보가 일치하지 않습니다.");
        }

    }
}
