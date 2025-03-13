package com.beyond.backend.domain.project.dto;

import java.util.List;

import com.beyond.backend.domain.project.entity.ProjectStatus;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


@Data
public class ProjectRequestDto {

    @NotBlank(message= "프로젝트 제목은 필수 입력 항목입니다.")
    private String name;

    @NotBlank(message= "프로젝트 내용은 필수 입력 항목입니다.")
    private String content;

    private Long teamNo;

    // private Long userNo;

    private ProjectStatus projectStatus;

    private List<Long> techsNos;
}

