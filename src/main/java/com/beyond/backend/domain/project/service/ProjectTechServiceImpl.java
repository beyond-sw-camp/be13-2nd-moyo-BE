package com.beyond.backend.domain.project.service;

import com.beyond.backend.domain.project.dto.ProjectTechResponseDto;
import com.beyond.backend.domain.project.entity.ProjectTech;
import com.beyond.backend.domain.project.repository.ProjectRepository;
import com.beyond.backend.domain.project.repository.ProjectTechRepository;
import com.beyond.backend.domain.tech.entity.Tech;
import com.beyond.backend.domain.tech.repository.TechRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ProjectTechServiceImpl {

    private final ProjectTechRepository projectTechRepository;
    private final ProjectRepository projectRepository;
    private final TechRepository techRepository;

    private ProjectTechResponseDto addProjectTech(Long projectNo, Long techNo) {
        projectRepository.findById(projectNo)
                .orElseThrow(() -> new IllegalArgumentException("Project not found"));

        Tech tech = techRepository.findById(techNo)
                .orElseThrow(() -> new IllegalArgumentException("Tech not found"));

        ProjectTech projectTech = ProjectTech.builder()
                .projectNo(projectNo)
                .tech(tech)
                .build();



        return new ProjectTechResponseDto(projectTech);
    }
}
