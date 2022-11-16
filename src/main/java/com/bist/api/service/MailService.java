package com.bist.api.service;

public interface MailService {
    void sendMail(String mailAddress, String subject, String message);
}
