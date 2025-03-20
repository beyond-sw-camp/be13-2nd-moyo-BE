package com.beyond.backend.controller;

import com.beyond.backend.domain.project.dto.ProjectRequestDto;
import com.beyond.backend.domain.project.dto.ProjectResponseDto;
import com.beyond.backend.domain.project.dto.ProjectUpdateRequestDto;
import com.beyond.backend.domain.project.entity.ProjectSearchOption;
import com.beyond.backend.domain.project.entity.ProjectStatus;
import com.beyond.backend.domain.project.service.ProjectService;
import com.beyond.backend.domain.teamUser.repository.TeamUserRepository;
import com.beyond.backend.domain.user.dto.CustomUserDetails;
import com.beyond.backend.domain.user.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "06 프로젝트 API", description = "프로젝트 API")
@RestController
@RequestMapping("/project")
@RequiredArgsConstructor
@Log4j2
public class ProjectController {

    private final ProjectService projectService;
    private final TeamUserRepository teamUserRepository;
    private final AuthService authService;


    @Operation(summary = "프로젝트 등록 메서드", description = "프로젝트 등록 메서드입니다.")
    @PostMapping()
    public ResponseEntity<ProjectResponseDto> createProject(@Valid @RequestBody ProjectRequestDto projectRequestDto) {

        ProjectResponseDto projectResponseDto = projectService.createProject(projectRequestDto);
        return ResponseEntity.ok(projectResponseDto);
    }

    @Operation(summary = "프로젝트 수정 메서드", description = "프로젝트 수정 메서드입니다.")
    @PostMapping("/{projectNo}")
    public ResponseEntity<ProjectResponseDto> updateProject(@RequestParam ProjectStatus projectStatus,
                                                            @PathVariable("projectNo") Long projectNo,
                                                            @Valid @RequestBody ProjectUpdateRequestDto projectRequestDto) {

        ProjectResponseDto projectResponseDto = projectService.updateProject(projectNo, projectStatus, projectRequestDto);

        return ResponseEntity.status(HttpStatus.OK).body(projectResponseDto);
    }

    @Operation(summary = "프로젝트 삭제 메서드", description = "프로젝트 삭제 메서드 입니다.")
    @DeleteMapping("/{projectNo}")
    public ResponseEntity<Void> deleteProject(
            @PathVariable("projectNo") Long projectNo) throws Exception {

        projectService.deleteProject(projectNo);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "프로젝트 단건 조회")
    @GetMapping("/{projectNo}")
    public ResponseEntity<ProjectResponseDto> getProjectByNo(@PathVariable("projectNo") Long projectNo,
                                                             HttpServletRequest request){
        projectService.viewProject(projectNo, request);
        ProjectResponseDto projectByProjectNo = projectService.getProjectByProjectNo(projectNo);

        return ResponseEntity.ok(projectByProjectNo);
    }


    @Operation(summary = "사용자의 모든 프로젝트 조회")
    @GetMapping("/user")
    public ResponseEntity<Page<ProjectResponseDto>> getProject( @AuthenticationPrincipal CustomUserDetails userDetails ,
                                                                @PageableDefault(size = 10, page = 0) Pageable pageable ){

        Page<ProjectResponseDto> projectsByUserNo = projectService.getProjectsByUserNo(userDetails.getUser().getNo(), pageable);

        return ResponseEntity.ok(projectsByUserNo);
    }

    @Operation(summary = "프로젝트 검색 및 전체 조회 ")
    @GetMapping("/search")
    public ResponseEntity<Page<ProjectResponseDto>> searchPosts(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) ProjectSearchOption option, // 검색 옵션 (선택)
            @PageableDefault(size = 10, page = 0) Pageable pageable) {

        Page<ProjectResponseDto> searchResults = projectService.searchProject(keyword, option, pageable);

        return ResponseEntity.ok(searchResults);
    }
}
