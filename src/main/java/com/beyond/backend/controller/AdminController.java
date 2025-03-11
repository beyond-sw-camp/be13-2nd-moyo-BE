package com.beyond.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.beyond.backend.domain.user.dto.CustomUserDetails;
import com.beyond.backend.domain.user.dto.DeleteUserByAdminRequestDto;
import com.beyond.backend.domain.user.dto.DeleteUserByAdminResponseDto;
import com.beyond.backend.domain.user.service.AdminService;
import com.beyond.backend.domain.user.service.AuthService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.RequiredArgsConstructor;

@RestController("/amdin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;
    private final AuthService authService;


    @PostMapping("/deleteuser")
    public ResponseEntity<DeleteUserByAdminResponseDto> deleteUserByAdmin(@RequestBody DeleteUserByAdminRequestDto dto,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
 
        CustomUserDetails currentUser = authService.getCurrentUser();
 
        if (authService.isAdminFromUserDetails(currentUser)) {
            adminService.DeleteUserByAdmin(dto);
            DeleteUserByAdminResponseDto response = new DeleteUserByAdminResponseDto("User deleted successfully.");
            return ResponseEntity.ok(response);
        } else {
            DeleteUserByAdminResponseDto response = new DeleteUserByAdminResponseDto("Unauthorized access.");
            return ResponseEntity.status(403).body(response);
        }
    }

}
