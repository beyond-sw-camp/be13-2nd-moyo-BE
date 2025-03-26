package com.beyond.backend.controller;

import com.beyond.backend.domain.common.dto.EmailAuthRequestDto;
import com.beyond.backend.domain.common.dto.EmailAuthResponseDto;
import com.beyond.backend.domain.common.service.EmailService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/email")
public class EmailController {

    private final EmailService emailService;

    // 인증번호 전송
    @PostMapping("/send")
    public EmailAuthResponseDto sendAuthCode(@Valid @RequestBody EmailAuthRequestDto dto) {
        return emailService.sendEmail(dto);
    }

    // 인증번호 검증
    @PostMapping("/validate")
    public EmailAuthResponseDto checkAuthCode(@Valid @RequestBody EmailAuthRequestDto dto,
                                              @RequestParam String authCode) {
        return emailService.validateAuthCode(dto, authCode);
    }

}