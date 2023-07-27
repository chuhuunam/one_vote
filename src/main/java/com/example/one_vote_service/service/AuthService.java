package com.example.one_vote_service.service;

import com.example.one_vote_service.common.ServiceResponseMessage;
import com.example.one_vote_service.config.constant.Channel;
import com.example.one_vote_service.config.security.AccountDetail;
import com.example.one_vote_service.domain.dto.auth.DetailGroupDto;
import com.example.one_vote_service.domain.dto.auth.GroupDto;
import com.example.one_vote_service.domain.dto.auth.GroupPageFeatureDto;
import com.example.one_vote_service.domain.reponse.LoginResponse;
import com.example.one_vote_service.domain.request.auth.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AuthService {
    AccountDetail findById(Integer userId, String jwt);

    ServiceResponseMessage register(RegisterRequest registerRequest);

    LoginResponse login(LoginRequest loginRequest, Channel channel);

    ServiceResponseMessage logout(AccountDetail accountDetail, Channel channel);

    GroupPageFeatureDto pageFeature(Channel channel, AccountDetail accountDetail);

    List<GroupDto> getListGroup(Channel channel);

    List<DetailGroupDto> getDetailGroup(Channel channel, Long groupId);

    ServiceResponseMessage addGroup(Channel channel, AddGroupRequest addGroupRequest);

    ServiceResponseMessage updateGroup(Channel channel, UpdateGroupRequest updateGroupRequest,Long groupId);

    ServiceResponseMessage deleteGroup(Long groupId);

    ServiceResponseMessage changePassword(UserChangePassRequest userChangePassRequest, AccountDetail accountDetail);

    ServiceResponseMessage resetPassword(Long userId);

    ServiceResponseMessage forgetPass(UserForgetPassRequest userForgetPassRequest);

    ResponseEntity<LoginResponse> refreshToken(Channel channel, RefreshTokenRequest refreshTokenRequest);
}
