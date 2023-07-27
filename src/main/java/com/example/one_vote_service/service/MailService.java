package com.example.one_vote_service.service;

import com.example.one_vote_service.domain.entity.auth.User;
import com.example.one_vote_service.domain.request.auth.MailForgetPassRequest;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;

@Service
public interface MailService {

    void SendMail(User user,String password) throws MessagingException;

}
