package com.beyond.backend.controller;

import com.beyond.backend.domain.project.dto.ProjectRequestDto;
import com.beyond.backend.domain.project.dto.ProjectResponseDto;
import com.beyond.backend.domain.project.dto.ProjectUpdateRequestDto;
import com.beyond.backend.domain.project.entity.ProjectSearchOption;
import com.beyond.backend.domain.project.entity.ProjectSortOption;
import com.beyond.backend.domain.project.entity.ProjectStatus;
import com.beyond.backend.domain.project.service.ProjectService;
import com.beyond.backend.domain.teamUser.repository.TeamUserRepository;
import com.beyond.backend.domain.user.dto.CustomUserDetails;

import com.beyond.backend.domain.user.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "프로젝트 API", description = "프로젝트 API")
@RestController
@RequestMapping("/project")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;
    private final TeamUserRepository teamUserRepository;
    private final AuthService authService;


    @Operation(summary = "프로젝트 등록 메서드", description = "프로젝트 등록 메서드입니다.")
    @PostMapping()
    public ResponseEntity<ProjectResponseDto> createProject(@Valid @RequestBody ProjectRequestDto projectRequestDto,
                                                            @AuthenticationPrincipal CustomUserDetails userDetails ) {

        ProjectResponseDto projectResponseDto = projectService.createProject(projectRequestDto, userDetails.getUser().getNo());

        return ResponseEntity.ok(projectResponseDto);
    }

    @Operation(summary = "프로젝트 수정 메서드", description = "프로젝트 수정 메서드입니다.")
    @PostMapping("/{projectNo}")
    public ResponseEntity<ProjectResponseDto> updateProject(@RequestParam ProjectStatus projectStatus,
                                                            @PathVariable("projectNo") Long projectNo,
                                                            @Valid @RequestBody ProjectUpdateRequestDto projectRequestDto,
                                                            @AuthenticationPrincipal CustomUserDetails userDetails ) {

        ProjectResponseDto projectResponseDto = projectService.updateProject(projectNo, projectStatus, projectRequestDto, userDetails.getUser().getNo() );

        return ResponseEntity.status(HttpStatus.OK).body(projectResponseDto);
    }

    @Operation(summary = "프로젝트 삭제 메서드", description = "프로젝트 삭제 메서드 입니다.")
    @DeleteMapping("/{projectNo}")
    public ResponseEntity<Void> deleteProject(  @AuthenticationPrincipal CustomUserDetails userDetails ,
                                                @PathVariable("projectNo") Long projectNo ) throws Exception {

        authService.validateAdminAuthorization();
        projectService.deleteProject(userDetails.getUser().getNo(), projectNo);

        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "프로젝트 전체 조회")
    @GetMapping
    public ResponseEntity<Page<ProjectResponseDto>> getProjects(@PageableDefault(size = 10, page = 0) Pageable pageable,
                                                                @RequestParam(required = false) ProjectSortOption projectSortOption) {

        Page<ProjectResponseDto> allProjects = projectService.getAllProjects(pageable, projectSortOption);

        if (allProjects.isEmpty())
            System.out.println("프로젝트가 비어있습니다.");

        return ResponseEntity.ok(allProjects);
    }


    @Operation(summary = "프로젝트 단건 조회")
    @GetMapping("/{projectNo}")
    public ResponseEntity<ProjectResponseDto> getProjectByNo(@PathVariable("projectNo") Long projectNo){
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

    @Operation(summary = "프로젝트 검색 ")
    @GetMapping("/search")
    public ResponseEntity<Page<ProjectResponseDto>> searchPosts( @RequestParam String keyword, @RequestParam(required = false) ProjectSearchOption option, // 검색 옵션 (선택)
        @PageableDefault(size = 10, page = 0) Pageable pageable) {

        Page<ProjectResponseDto> searchResults = projectService.searchProject(keyword, option, pageable);

        return ResponseEntity.ok(searchResults);
    }
}
