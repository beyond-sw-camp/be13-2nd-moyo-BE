package com.beyond.backend.controller;

import com.beyond.backend.data.dto.ProjectDto;
import com.beyond.backend.data.dto.ProjectResponseDto;
import com.beyond.backend.service.ProjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    @Operation(summary = "프로젝트 조회 메서드", description = "프로젝트 조회 메서드입니다.")
    @GetMapping("/{teamNo}")
    public ResponseEntity<List<ProjectResponseDto>> getProjectsByTeamNo(@PathVariable Long teamNo, Pageable pageable) {
        Page<ProjectResponseDto> projects = projectService.getProjectsByTeamNo(teamNo, pageable);

        return ResponseEntity.status(HttpStatus.OK).body(projects.getContent());
    }


    @Operation(summary = "프로젝트 등록 메서드", description = "프로젝트 등록 메서드입니다.")
    @PostMapping()
    public ResponseEntity<ProjectResponseDto> createProject(@RequestBody ProjectDto projectDto) {

        ProjectResponseDto projectResponseDto = projectService.createProject(projectDto);

        return ResponseEntity.status(HttpStatus.OK).body(projectResponseDto);
    }


    @Operation(summary = "프로젝트 삭제 메서드", description = "프로젝트 삭제 메서드 입니다.")
    @DeleteMapping
    public ResponseEntity<String> deleteProject(Long id) throws Exception {
        projectService.deleteProject(id);

        return ResponseEntity.status(HttpStatus.OK).body("정상적으로 삭제되었습니다.");
    }
}
