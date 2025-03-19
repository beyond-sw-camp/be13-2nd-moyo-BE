package com.beyond.backend.domain.common.service;

import com.beyond.backend.domain.common.dto.EmailAuthResponseDto;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.concurrent.ThreadLocalRandom;

/**
 * MimeMessage : 이메일의 내용과 형식을 정의
 * - Mime : 이메일에서 텍스트 외의 다양한 형식을 지원하기 위한 인터넷 표준
 * JavaMailSender : 이메일 전송을 위한 인터페이스
 * - 실제 이메일 발송 기능을 제공
 */
@Service
@RequiredArgsConstructor
public class EmailService {

    @Value("${spring.mail.username}")
    private String senderEmail;

    private final JavaMailSender mailSender;
    private final RedisUtil redisUtil;

    public EmailAuthResponseDto sendEmail(String toEmail) {
        if (redisUtil.existData(toEmail)) {
            redisUtil.deleteData(toEmail); //기존 인증 코드가 있으면 선택
        }
        try {
            MimeMessage emailForm = createEmailForm(toEmail);
            mailSender.send(emailForm); //실제 이메일 발송
            return new EmailAuthResponseDto(true, "인증번호가 메일로 전송되었습니다.");
        } catch (MessagingException | MailSendException e) {
            return new EmailAuthResponseDto(false, "메일 전송 중 오류가 발생하였습니다. 다시 시도해주세요.");
        }
    }

    private MimeMessage createEmailForm(String email) throws MessagingException {

        String authCode = String.valueOf(ThreadLocalRandom.current().nextInt(100000, 1000000));

        MimeMessage message = mailSender.createMimeMessage();

        message.setFrom(senderEmail); //발신자 설정
        message.setRecipients(MimeMessage.RecipientType.TO, email); //수신자 설정
        message.setSubject("인증코드입니다."); //제목
        message.setText(setContext(authCode), "utf-8", "html"); //HTML 형식 내용 설정

        redisUtil.setDataExpire(email, authCode, 10 * 60L); // 인증코드 10분간 저장

        return message;
    }

    private String setContext(String authCode) {
        String body = "";
        body += "<h4>" + "인증 코드를 입력하세요." + "</h4>";
        body += "<h2>" + "[" + authCode + "]" + "</h2>";
        return body;
    }

    public EmailAuthResponseDto validateAuthCode(String email, String authCode) {
        String findAuthCode = redisUtil.getData(email);
        if (findAuthCode == null) { //Redis에서 인증 코드 만료
            return new EmailAuthResponseDto(false, "인증번호가 만료되었습니다. 다시 시도해주세요.");
        }

        if (findAuthCode.equals(authCode)) { //인증 코드 일치
            return new EmailAuthResponseDto(true, "인증 성공에 성공했습니다.");
        } else { //인증 코드 불일치
            return new EmailAuthResponseDto(false, "인증번호가 일치하지 않습니다.");
        }
    }
}
