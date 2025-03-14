package com.beyond.backend.controller;

import com.beyond.backend.domain.message.dto.MessageDto;
import com.beyond.backend.domain.message.dto.MessageResponseDto;
import com.beyond.backend.domain.message.service.MessageService;
import com.beyond.backend.domain.user.dto.CustomUserDetails;
import com.beyond.backend.domain.user.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<MessageResponseDto> getMessage(@AuthenticationPrincipal CustomUserDetails userDetails, @RequestParam Long messageNo) {
        MessageResponseDto messageResponseDto = messageService.getMessage(userDetails.getUser().getNo(), messageNo);

        return ResponseEntity.status(HttpStatus.OK).body(messageResponseDto);
    }


    /**
     * 쪽지 전송
     */
    @Operation(summary = "쪽지 전송", description = "쪽지를 전송(저장)합니다")
    @PostMapping("/messages")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<MessageResponseDto> sendMessage(@AuthenticationPrincipal CustomUserDetails userDetails, @Valid @RequestBody MessageDto messageDto) { // senderNo는 토큰으로 어찌저찌 하기
        MessageResponseDto messageResponseDto = messageService.messageWrite(userDetails.getUser(), messageDto);

        return ResponseEntity.status(HttpStatus.OK).body(messageResponseDto);
    }

    /**
     * 쪽지 조회 리스트
     */
    @Operation(summary = "쪽지리스트")
    @GetMapping("/messages/{type}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Page<MessageResponseDto>> getMessages(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Parameter(name = "type", description = "보낸/받은 쪽지리스트 (sent/received)", example = "sent") @PathVariable String type,
            @PageableDefault(size = 10, page = 0, sort = "no") Pageable pageable) {


        Page<MessageResponseDto> messageResponseDto;

        if ("sent".equalsIgnoreCase(type)) {
            messageResponseDto = messageService.getSentMessageList(userDetails.getUser().getNo(), pageable);
        } else {
            messageResponseDto = messageService.getReceivedMessageList(userDetails.getUser().getNo(), pageable);
        }

        return ResponseEntity.ok(messageResponseDto);
    }

    /**
     * 쪽지 삭제
     */
    @Operation(summary = "쪽지 삭제", description = "쪽지를 삭제합니다.")
    @DeleteMapping("/messages")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<MessageResponseDto> deleteMessage
    (@AuthenticationPrincipal CustomUserDetails userDetails, @RequestParam Long messageNo) {
        messageService.deleteMessage(userDetails.getUser().getNo(), messageNo);

        MessageResponseDto responseDto = new MessageResponseDto("삭제되었습니다");
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    /**
     * 안읽음 카운트
     */
    @Operation(summary = "안 읽은 쪽지 개수", description = "안 읽은 쪽지 개수를 확인합니다")
    @GetMapping("/messages-unread")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Long> getUnreadMessage(@AuthenticationPrincipal CustomUserDetails userDetails) {

        long unreadCount = messageService.getUnreadMessageCount(userDetails.getUser().getNo());
        return ResponseEntity.ok(unreadCount);
    }

}
