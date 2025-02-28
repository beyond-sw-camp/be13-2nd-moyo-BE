package com.beyond.backend.controller;

import com.beyond.backend.data.dto.MessageDto;
import com.beyond.backend.data.dto.MessageResponseDto;
import com.beyond.backend.data.entity.User;
import com.beyond.backend.data.repository.UserRepository;
import com.beyond.backend.service.MessageService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MessageController {
    private final MessageService messageService;
    private final UserRepository userRepository;

    //    @Operation(summary = "쪽지 단일 조회", description = "쪽지를 조회합니다.")
//    @GetMapping("/read/{userNo}/{messageNo}")
//    public ResponseEntity<MessageResponseDto> getMessage(@PathVariable Long messageNo, @PathVariable Long userNo) {
//        MessageResponseDto messageResponseDto = messageService.getMessage(messageNo, userNo);
//
//        return ResponseEntity.status(HttpStatus.OK).body(messageResponseDto);
//    }
    @Operation(summary = "쪽지 단일 조회", description = "쪽지를 조회합니다.")
    @GetMapping("/messages")
    public ResponseEntity<MessageResponseDto> getMessage(@RequestParam Long userNo, @RequestParam Long messageNo) {
        MessageResponseDto messageResponseDto = messageService.getMessage(userNo, messageNo);

        return ResponseEntity.status(HttpStatus.OK).body(messageResponseDto);
    }

    @Operation(summary = "쪽지 전송", description = "쪽지를 전송(저장)합니다")
    @PostMapping("/messages")
    public ResponseEntity<MessageResponseDto> sendMessage(@RequestBody MessageDto messageDto) {
        MessageResponseDto messageResponseDto = messageService.messageWrite(messageDto);

        return ResponseEntity.status(HttpStatus.OK).body(messageResponseDto);
    }

    @Operation(summary = "보낸 쪽지 등록순", description = "보낸 쪽지를 등록순으로 조회합니다")
    @GetMapping("/messages/sent/{userNo}/order") // ?sort=order
    public ResponseEntity<List<MessageResponseDto>> getSentMessageByOrder(@PathVariable Long userNo) { // User 객체로 바꾸기
        User user = getUserByNo(userNo);
        List<MessageResponseDto> messageResponseDto = messageService.getSentMessagesByOrder(user.getNo());

        return ResponseEntity.ok(messageResponseDto);
    }

    @Operation(summary = "받은 쪽지 등록순", description = "받은 쪽지를 등록순으로 조회합니다.")
    @GetMapping("/messages/received/{userNo}/order")
    public ResponseEntity<List<MessageResponseDto>> getReceivedMessageByOrder(@PathVariable Long userNo) {
        User user = getUserByNo(userNo);
        List<MessageResponseDto> messageResponseDto = messageService.getReceivedMessagesByOrder(user.getNo());

        return ResponseEntity.ok(messageResponseDto);
    } // try catch 로..?

    @Operation(summary = "보낸 쪽지 최신순", description = "보낸 쪽지를 최신순으로 조회합니다.")
    @GetMapping("/messages/sent/{userNo}/latest")
    public ResponseEntity<List<MessageResponseDto>> getSentMessageByLatest(@PathVariable Long userNo) { // User 객체로 바꾸기
        User user = getUserByNo(userNo);
        List<MessageResponseDto> messageResponseDto = messageService.getSentMessagesByLatest(user.getNo());

        return ResponseEntity.ok(messageResponseDto);
    }

    @Operation(summary = "받은 쪽지 최신순", description = "받은 쪽지를 최신순으로 조회합니다.")
    @GetMapping("/messages/received/{userNo}/latest")
    public ResponseEntity<List<MessageResponseDto>> getReceivedMessageByLatest(@PathVariable Long userNo) {
        User user = getUserByNo(userNo);
        List<MessageResponseDto> messageResponseDto = messageService.getReceivedMessagesByLatest(user.getNo());

        return ResponseEntity.ok(messageResponseDto);
    }

    private User getUserByNo(Long userNo) {
        return userRepository.findByNo(userNo)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }
    // try catch 로..?
/*
    @DeleteMapping("/delete/sent")
    public ResponseEntity<MessageResponseDto> deleteSentMessage(@RequestParam Long no, @RequestParam String userId) {
//        User user = userRepository.findByUserId(user.getNo())
//                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        // 현재 로그인 한 계정 번호

        messageService.deleteMessageBySender(no, userId);
        return deleteResponse();
    }

    @DeleteMapping("/delete/received")
    public ResponseEntity<MessageResponseDto> deleteReceivedMessage(@RequestParam Long no, @RequestParam String userId) {
//        User user = userRepository.findByUserId(user.getNo())
//                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        // 현재 로그인 한 계정 번호

        messageService.deleteMessageByReceiver(no, userId);
        return deleteResponse();
    }

    private ResponseEntity<MessageResponseDto> deleteResponse() {
        MessageResponseDto responseDto = MessageResponseDto.fromContent("삭제 되었습니다."); // 유저, 메시지 일치하지않는것 추가하기 일단 throw
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }
    */

    @Operation(summary = "쪽지 삭제", description = "쪽지를 삭제합니다.")
    @DeleteMapping("/messages")
    public ResponseEntity<MessageResponseDto> deleteMessage
            (@RequestParam Long userNo, @RequestParam Long messageNo) {
        getUserByNo(userNo);

        messageService.deleteMessage(userNo, messageNo);

        MessageResponseDto responseDto = new MessageResponseDto();
        responseDto.setContent("삭제되었습니다.");

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @Operation(summary = "안 읽은 쪽지 개수", description = "안 읽은 쪽지 개수를 확인합니다")
    @GetMapping("/messages-unread")
    public ResponseEntity<Long> getUnreadMessage(@RequestParam Long userNo) {
        if (getUserByNo(userNo) == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        long unreadCount = messageService.getUnreadMessageCount(userNo);
        return ResponseEntity.ok(unreadCount);
    }

//    @DeleteMapping("/delete")
//    public ResponseEntity<String> deleteMessage(Long id) throws Exception {
//        messageService.deleteMessage(id);
//
//        return ResponseEntity.status(HttpStatus.OK).body("정상적으로 삭제되었습니다");
//    }
}
