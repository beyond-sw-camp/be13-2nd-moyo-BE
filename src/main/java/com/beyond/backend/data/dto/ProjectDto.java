package com.beyond.backend.data.dto;

import java.util.List;

import com.beyond.backend.data.entity.ProjectStatus;
import com.beyond.backend.data.entity.ProjectTech;
import com.beyond.backend.data.entity.ProjectType;
import com.beyond.backend.data.entity.Team;

import lombok.*;

/**
 * <p>
 *
 * <p>packageName    : com.beyond.backend.data.dto
 * <p>fileName       : ProjectDto
 * <p>author         : jaewoo
 * <p>date           : 2025. 1. 31.
 * <p>description    : 프로젝트 DTO
 */
/*
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025. 1. 31.        jaewoo             최초 생성
 * 2025. 2. 2.         jaewoo             내용 수정
 * 2025. 2. 3.         jaewoo             변수명 수정
 * 2025. 2. 4.         jaewoo             변수명 수정
 */

@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
public class ProjectDto {

    private String name;

    private String content;

    private Team team;

    private String projectPurpose;

    private String projectSubject;

    private ProjectStatus projectStatus;

    // tech 수정 안돼!!!!!!!!!!!!!!!!!!!!!!!!! 왜냐면 담당자가 없어용 ~~
    // private List<ProjectTech> projectTeches;
}
