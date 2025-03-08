package com.beyond.backend.domain.project.dto;

import com.beyond.backend.domain.project.entity.ProjectStatus;

import com.beyond.backend.domain.project.entity.ProjectTech;
import com.beyond.backend.domain.tech.entity.Tech;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;


@Data
public class ProjectRequestDto {

    private String name;

    private String content;

    private Long teamNo;

    private ProjectStatus projectStatus;


    private List<Long> techsNos;
}

