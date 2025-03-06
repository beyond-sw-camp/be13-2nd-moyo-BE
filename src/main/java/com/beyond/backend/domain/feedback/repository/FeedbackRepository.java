package com.beyond.backend.domain.feedback.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.beyond.backend.domain.feedback.entity.Feedback;

public interface FeedbackRepository extends JpaRepository<Feedback, Long>, FeedbackRepositoryCustom {
}
