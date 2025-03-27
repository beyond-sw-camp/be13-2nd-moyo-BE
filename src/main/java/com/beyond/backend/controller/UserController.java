package com.beyond.backend.controller;

import com.beyond.backend.domain.user.dto.*;
import com.beyond.backend.domain.user.service.AuthService;
import com.beyond.backend.domain.user.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "02 유저 API", description = "유저 API")
@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final AuthService authService;

    @PostMapping("/{username}/ban")
    public ResponseEntity<BanResponseDto> ban(@Valid @RequestBody BanRequestDto dto) {

        BanResponseDto banResponseDto = userService.banUser(dto);
        log.info("{}을 banned = {} 로 바꿈", dto.getUsername(), dto.getBan());
        return ResponseEntity.ok(banResponseDto);
    }

    @PostMapping("/unlock/{username}")
    public ResponseEntity<UnlockResponseDto> unlock(@PathVariable String username) {
        UnlockResponseDto unlockResponseDto = userService.unlockUser(username);
        log.info("{}을 unlock!", username);
        return ResponseEntity.ok(unlockResponseDto);
    }

    @PostMapping("/update")
    public ResponseEntity<UserUpdateResponseDto> update(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody UserUpdateRequestDto dto){
        UserUpdateResponseDto userUpdateResponseDto = userService.updateUser(userDetails.getNo(), dto);
        return ResponseEntity.ok(userUpdateResponseDto);
    }

    @PostMapping("/updatePassword")
    public ResponseEntity<PasswordUpdateResponseDto> updatePassword(
            @Valid @RequestBody PasswordUpdateRequestDto dto,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        String username = userDetails.getUsername();
        PasswordUpdateResponseDto response = userService.updatePassword(username, dto);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/updatePasswordForUnlock/{username}")
    public ResponseEntity<PasswordUpdateResponseDto> updatePassword
            (@Valid @RequestBody PasswordUpdateRequestDto dto,
             @PathVariable String username) {
        PasswordUpdateResponseDto response = userService.updatePassword(username, dto);
        userService.unlockUser(username);
        return ResponseEntity.ok(response);
    }


    @PostMapping("/delete")
    public ResponseEntity<Void> delete(String username) {
        userService.deleteUser(username);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/user/{username}")
    public ResponseEntity<OneUserResponseDto> getUserNo(@AuthenticationPrincipal CustomUserDetails userDetails) {
        OneUserResponseDto allUserResponseDto = userService.getUserByUsername(userDetails);

        return ResponseEntity.ok(allUserResponseDto);
    }

    @PostMapping("/matchEmail")
    public ResponseEntity<Void> matchEmail(@RequestBody MatchEmailRequestDto dto) {
        authService.validateEmail(dto.getUsername(), dto.getEmail());
        return ResponseEntity.ok().build();
    }
}
