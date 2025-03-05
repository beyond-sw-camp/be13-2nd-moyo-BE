package com.beyond.backend.controller;

import com.beyond.backend.data.dto.MessageDto;
import com.beyond.backend.data.dto.MessageResponseDto;
import com.beyond.backend.data.entity.User;
import com.beyond.backend.data.repository.UserRepository;
import com.beyond.backend.service.MessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    /**
     * 쪽지 단일 조회
     */
    @Operation(summary = "쪽지 단일 조회", description = "쪽지를 조회합니다.")
    @GetMapping("/messages")
    public ResponseEntity<MessageResponseDto> getMessage(@RequestParam Long userNo, @RequestParam Long messageNo) {
        MessageResponseDto messageResponseDto = messageService.getMessage(userNo, messageNo);

        return ResponseEntity.status(HttpStatus.OK).body(messageResponseDto);
    }

    /**
     * 쪽지 전송
     */
    @Operation(summary = "쪽지 전송", description = "쪽지를 전송(저장)합니다")
    @PostMapping("/messages")
    public ResponseEntity<MessageResponseDto> sendMessage(@RequestBody MessageDto messageDto) {
        MessageResponseDto messageResponseDto = messageService.messageWrite(messageDto);

        return ResponseEntity.status(HttpStatus.OK).body(messageResponseDto);
    }

    //


    /**
     * 쪽지 조회 리스트
     */
    @Operation(summary = "쪽지리스트")
    @GetMapping("/messages/{type}/{userNo}")
    public ResponseEntity<Page<MessageResponseDto>> getMessages(
            @PathVariable Long userNo,
            @Parameter(name = "type", description = "보낸/받은 쪽지리스트 (sent/received)", example = "sent") @PathVariable String type,
            @PageableDefault(size = 10, page = 0, sort = "no") Pageable pageable) {

        User user = getUserByNo(userNo);
        Page<MessageResponseDto> messageResponseDto;

        if ("sent".equalsIgnoreCase(type)) {
            messageResponseDto = messageService.getSentMessageList(user.getNo(), pageable);
        } else {
            messageResponseDto = messageService.getReceivedMessageList(user.getNo(), pageable);
        }

        return ResponseEntity.ok(messageResponseDto);
    }

    /**
     * 쪽지 삭제
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

    /**
     * 안읽음 카운트
     */
    @Operation(summary = "안 읽은 쪽지 개수", description = "안 읽은 쪽지 개수를 확인합니다")
    @GetMapping("/messages-unread")
    public ResponseEntity<Long> getUnreadMessage(@RequestParam Long userNo) {
        if (getUserByNo(userNo) == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        long unreadCount = messageService.getUnreadMessageCount(userNo);
        return ResponseEntity.ok(unreadCount);
    }

//    @Operation(summary = "보낸쪽지리스트")
//    @GetMapping("/messages/sent/{userNo}")
//    public ResponseEntity<Page<MessageResponseDto>> getSentMessages(
//            @PathVariable Long userNo,
//            @Parameter(name = "page", description = "페이지 번호", example = "0") @RequestParam(defaultValue = "0") int page,
//            @Parameter(name = "size", description = "페이지 크기", example = "10") @RequestParam(defaultValue = "10") int size,
//            @Parameter(name = "sort", description = "정렬 필드", example = "no") @RequestParam(defaultValue = "no") String sort) { // User 객체로 바꾸기
//        Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
//        User user = getUserByNo(userNo);
//        Page<MessageResponseDto> messageResponseDto = messageService.getSentMessageList(user.getNo(), pageable);
//
//        return ResponseEntity.ok(messageResponseDto);
//    }
//
//    @Operation(summary = "받은쪽지리스트")
//    @GetMapping("/messages/received/{userNo}")
//    public ResponseEntity<Page<MessageResponseDto>> getReceivedMessages(
//            @PathVariable Long userNo,
//            @Parameter(name = "page", description = "페이지 번호", example = "0") @RequestParam(defaultValue = "0") int page,
//            @Parameter(name = "size", description = "페이지 크기", example = "10") @RequestParam(defaultValue = "10") int size,
//            @Parameter(name = "sort", description = "정렬 필드", example = "no") @RequestParam(defaultValue = "no") String sort
//    ) { // User 객체로 바꾸기
//        Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
//        User user = getUserByNo(userNo);
//        Page<MessageResponseDto> messageResponseDto = messageService.getReceivedMessageList(user.getNo(), pageable);
//
//        return ResponseEntity.ok(messageResponseDto);
//    }

    //

    /**
     * 유저 있는지 메서드
     */
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


//    @DeleteMapping("/delete")
//    public ResponseEntity<String> deleteMessage(Long id) throws Exception {
//        messageService.deleteMessage(id);
//
//        return ResponseEntity.status(HttpStatus.OK).body("정상적으로 삭제되었습니다");
//    }
}
