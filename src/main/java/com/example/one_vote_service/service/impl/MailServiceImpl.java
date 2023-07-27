package com.example.one_vote_service.service.impl;

import com.example.one_vote_service.domain.entity.auth.User;
import com.example.one_vote_service.service.MailService;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.HashMap;
import java.util.Map;

@Service
public class MailServiceImpl implements MailService {

    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;

    public MailServiceImpl(
            JavaMailSender mailSender,
            SpringTemplateEngine templateEngine
    ) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }

    @Override
    public void SendMail(User user, String password) throws MessagingException {
        Map<String, Object> props = new HashMap<>();
        props.put("fullName", user.getFullName());
        props.put("password", password);
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8");
        Context context = new Context();
        context.setVariables(props);
        String html = templateEngine.process("client", context);
        helper.setTo(user.getEmail());
        helper.setSubject("Thông tin tài khoản cá nhân");
        helper.setText(html, true);
        mailSender.send(message);
    }
}
