package com.beyond.backend.controller;

import com.beyond.backend.domain.user.dto.*;
import com.beyond.backend.domain.user.entity.User;
import com.beyond.backend.domain.user.repository.UserRepository;
import com.beyond.backend.domain.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/{username}/ban")
    public ResponseEntity<BanResponseDto> ban(@Valid @RequestBody BanRequestDto dto) {

        BanResponseDto banResponseDto = userService.banUser(dto);
        log.info("{}을 banned = {} 로 바꿈", dto.getUsername(), dto.getBan());
        return ResponseEntity.ok(banResponseDto);
    }

    @PostMapping("/unlock")
    public ResponseEntity<UnlockResponseDto> unlock(@Valid @RequestBody UnlockRequestDto dto) {
        UnlockResponseDto unlockResponseDto = userService.unlockUser(dto);
        log.info("{}을 unlock!", dto.getUsername());
        return ResponseEntity.ok(unlockResponseDto);
    }

    @PostMapping("/updatePassword")
    public ResponseEntity<PasswordUpdateResponseDto> updatePassword(@Valid @RequestBody PasswordUpdateRequestDto dto,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        String username = userDetails.getUsername();
        PasswordUpdateResponseDto response = userService.updatePassword(username, dto);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/delete")
    public ResponseEntity<Void> delete(String username) {
        userService.deleteUser(username);
        return ResponseEntity.ok().build();
    }
}