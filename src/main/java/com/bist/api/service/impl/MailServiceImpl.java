package com.bist.api.service.impl;

import com.bist.api.service.MailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {

    private final JavaMailSender mailSender;

    @Override
    public void sendMail(String mailAddress, String subject, String message) {
        log.info("Mail sent to: " + mailAddress);
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo(mailAddress);
        mail.setSubject(subject);
        mail.setText(message);
        mailSender.send(mail);
        log.info("Mail sent to: " + mailAddress);

    }
}
