package com.beyond.backend.controller;

import com.beyond.backend.domain.user.dto.PasswordUpdateRequestDto;
import com.beyond.backend.domain.user.dto.PasswordUpdateResponseDto;
import com.beyond.backend.domain.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/updatePassword")
    public ResponseEntity<PasswordUpdateResponseDto> updatePassword(
            @Valid @RequestBody PasswordUpdateRequestDto dto) {

        PasswordUpdateResponseDto response = userService.updatePassword(dto);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/delete")
    public ResponseEntity<Void> delete(String username) {
        userService.deleteUser(username);
        return ResponseEntity.ok().build();
    }
}