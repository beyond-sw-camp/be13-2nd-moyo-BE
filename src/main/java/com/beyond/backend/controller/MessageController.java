package com.beyond.backend.controller;

import com.beyond.backend.domain.message.dto.MessageDto;
import com.beyond.backend.domain.message.dto.MessageResponseDto;
import com.beyond.backend.domain.message.service.MessageService;
import com.beyond.backend.domain.user.entity.User;
import com.beyond.backend.domain.user.repository.UserRepository;
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
    /**
     * user null(로그인 안 했을때 처리)
     */
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
    public ResponseEntity<MessageResponseDto> sendMessage(@RequestParam Long senderNo, @RequestBody MessageDto messageDto) { // senderNo는 토큰으로 어찌저찌 하기
        MessageResponseDto messageResponseDto = messageService.messageWrite(senderNo, messageDto);

        return ResponseEntity.status(HttpStatus.OK).body(messageResponseDto);
    }

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

        MessageResponseDto responseDto = new MessageResponseDto("삭제되었습니다");
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


    /**
     * 유저 있는지 메서드
     */
    private User getUserByNo(Long userNo) {
        return userRepository.findById(userNo)
                .orElseThrow(() -> new IllegalArgumentException("해당 회원을 찾을 수 없습니다."));
    }
}
