package com.beyond.backend.controller;

import com.beyond.backend.domain.message.dto.MessageDto;
import com.beyond.backend.domain.message.dto.MessageResponseDto;
import com.beyond.backend.domain.message.service.MessageService;
import com.beyond.backend.domain.user.dto.CustomUserDetails;
import com.beyond.backend.domain.user.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
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

@Tag(name = "09 쪽지 API", description = "쪽지 API")
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
    @GetMapping("/messages/{messageNo}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<MessageResponseDto> getMessage(@PathVariable Long messageNo,
                                                         @AuthenticationPrincipal CustomUserDetails userDetails) {
        MessageResponseDto messageResponseDto = messageService.getMessage(userDetails.getNo(), messageNo);

        return ResponseEntity.status(HttpStatus.OK).body(messageResponseDto);
    }


    /**
     * 쪽지 전송
     */
    @Operation(summary = "쪽지 전송", description = "쪽지를 전송(저장)합니다")
    @PostMapping("/messages")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<MessageResponseDto> sendMessage(@Valid @RequestBody MessageDto messageDto,
                                                          @AuthenticationPrincipal CustomUserDetails userDetails) { // senderNo는 토큰으로 어찌저찌 하기
        MessageResponseDto messageResponseDto = messageService.messageWrite(userDetails.getNo(), messageDto);

        return ResponseEntity.status(HttpStatus.OK).body(messageResponseDto);
    }

    /**
     * 쪽지 조회 리스트
     */
    @Operation(summary = "쪽지리스트")
    @GetMapping("/messages")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Page<MessageResponseDto>> getMessages(
            @Parameter(name = "type", description = "보낸/받은 쪽지리스트 (sent/received)", example = "sent")
            @RequestParam String type,
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PageableDefault(size = 10, page = 0, sort = "no") Pageable pageable) {

        Page<MessageResponseDto> messageResponseDto = messageService.getMessageList(userDetails.getNo(), type, pageable);
        return ResponseEntity.ok(messageResponseDto);
    }

    /**
     * 쪽지 삭제
     */
    @Operation(summary = "쪽지 삭제", description = "쪽지를 삭제합니다.")
    @DeleteMapping("/messages/{messageNo}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<MessageResponseDto> deleteMessage(@PathVariable Long messageNo,
                                                            @AuthenticationPrincipal CustomUserDetails userDetails) {
        messageService.deleteMessage(userDetails.getNo(), messageNo);

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

        long unreadCount = messageService.getUnreadMessageCount(userDetails.getNo());
        return ResponseEntity.ok(unreadCount);
    }
}
