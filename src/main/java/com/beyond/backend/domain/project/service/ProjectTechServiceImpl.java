package com.beyond.backend.domain.project.service;

import com.beyond.backend.domain.project.repository.ProjectRepository;
import com.beyond.backend.domain.project.repository.ProjectTechRepository;
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


}
