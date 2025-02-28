package com.beyond.backend.data.repository;

import java.util.List;

import com.beyond.backend.data.entity.Project;

public interface ProjectRepositoryCustom {
    List<Project> findProjectsByUserId(Long userNo);
}