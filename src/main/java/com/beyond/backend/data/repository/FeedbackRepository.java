package com.beyond.backend.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.beyond.backend.data.entity.Feedback;

public interface FeedbackRepository extends JpaRepository<Feedback, Long>, FeedbackRepositoryCustom {
}
