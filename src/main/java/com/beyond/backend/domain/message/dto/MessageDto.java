package com.beyond.backend.domain.message.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>
 * <p>packageName    : com.beyond.backend.data.dto
 * <p>fileName       : MessageResponseDto
 * <p>author         : mlnstone
 * <p>date           : 2025. 2. 15.
 * <p>description    :
 */
/*
===========================================================
DATE              AUTHOR             NOTE
-----------------------------------------------------------
2025. 2. 15.        mlnstone             최초 생성
*/

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageDto {
    private Long receiverNo;

    private String content;
}
