package com.beyond.backend.domain.common;

import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedBy;

@Getter
@ToString
@MappedSuperclass
public class AuditableEntity extends BaseEntity{

    @CreatedBy
    private String createdBy;
}