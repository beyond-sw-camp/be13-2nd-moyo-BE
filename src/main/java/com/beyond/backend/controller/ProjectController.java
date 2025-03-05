package com.beyond.backend.controller;

import com.beyond.backend.data.dto.ProjectRequestDto;
import com.beyond.backend.data.dto.ProjectResponseDto;
import com.beyond.backend.data.entity.ProjectSearchOption;
import com.beyond.backend.service.ProjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * <p>프로젝트 API
 *
 * <p>packageName    : com.beyond.backend.controller
 * <p>fileName       : ProjectController
 * <p>author         : jaewoo
 * <p>date           : 2025. 2. 2.
 * <p>description    : 프로젝트 API
 */
/*
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025. 2. 2.        jaewoo             최초 생성
 * 2025. 2. 3.        jaewoo             함수명 수정
 * 2025. 2. 4.        jaewoo             함수명 수정
 * 2025. 2. 17.       jaewoo             프로젝트 조회 로직 수정
 * 2025. 2. 18.       jaewoo             프로젝트 조회 페이징 처리
 */

@Tag(name = "프로젝트 API", description = "프로젝트 API")
@RestController
@RequestMapping("/project")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    /**
     * Get product response entity.
     *
     * @param teamNo 팀 번호
     * @return the response entity
     */

    @Operation(summary = "프로젝트 등록 메서드", description = "프로젝트 등록 메서드입니다.")
    @PostMapping("/create")
    public ResponseEntity<ProjectResponseDto> createProject(@RequestBody ProjectRequestDto projectRequestDto) {

        ProjectResponseDto projectResponseDto = projectService.createProject(projectRequestDto);

        return ResponseEntity.ok(projectResponseDto);
    }

    @Operation(summary = "프로젝트 수정 메서드", description = "프로젝트 수정 메서드입니다.")
    @PostMapping("/{userNo}/{projectNo}")
    public ResponseEntity<ProjectResponseDto> updateProject(@PathVariable("projectNo") Long projectNo, @PathVariable("userNo") Long userNo, @RequestBody ProjectRequestDto projectRequestDto) {

        ProjectResponseDto projectResponseDto = projectService.updateProject(projectNo, userNo , projectRequestDto);

        return ResponseEntity.status(HttpStatus.OK).body(projectResponseDto);
    }


    @Operation(summary = "프로젝트 삭제 메서드", description = "프로젝트 삭제 메서드 입니다.")
    @DeleteMapping("/{projectNo}")
    public ResponseEntity<Void> deleteProject( @PathVariable("projectNo") Long projectNo ) throws Exception {

        projectService.deleteProject(projectNo);

        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "프로젝트 전체 조회")
    @GetMapping
    public ResponseEntity<Page<ProjectResponseDto>> getProjects( @PageableDefault(size = 10, page = 0 ) Pageable pageable ){

        Page<ProjectResponseDto> allProjects = projectService.getAllProjects(pageable);


        if (allProjects.isEmpty())
            System.out.println("프로젝트가 비어있습니다.");

        return ResponseEntity.ok(allProjects);
    }


    @Operation(summary = "프로젝트 단건 조회")
    @GetMapping("/select/{projectNo}")
    public ResponseEntity<ProjectResponseDto> getProjectByNo(@PathVariable("projectNo") Long projectNo){
        ProjectResponseDto projectByProjectNo = projectService.getProjectByProjectNo(projectNo);

        return ResponseEntity.ok(projectByProjectNo);
    }


    @Operation(summary = "사용자의 모든 프로젝트 조회")
    @GetMapping("/users/{userNo}/projects")
    public ResponseEntity<Page<ProjectResponseDto>> getProject(@PathVariable("userNo") Long userNo, @PageableDefault(size = 10, page = 0) Pageable pageable ){

        Page<ProjectResponseDto> projectsByUserNo = projectService.getProjectsByUserNo(userNo, pageable);

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
