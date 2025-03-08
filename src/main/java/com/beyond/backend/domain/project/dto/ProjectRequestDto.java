package com.beyond.backend.domain.project.dto;

import com.beyond.backend.domain.project.entity.ProjectStatus;

import lombok.*;

import java.util.List;


@Data
public class ProjectRequestDto {

    private String name;

    private String content;

    private Long teamNo;

    private ProjectStatus projectStatus;

    private List<Long> techsNos;
}
