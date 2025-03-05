package com.beyond.backend.data.dto;

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
    private Long senderNo;
    private Long receiverNo;

    private String content;
    // sendAt, sender 아이디 필요한지?
}
