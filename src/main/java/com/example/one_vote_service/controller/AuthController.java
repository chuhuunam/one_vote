package com.example.one_vote_service.controller;

import com.example.one_vote_service.common.ServiceResponseMessage;
import com.example.one_vote_service.config.constant.Channel;
import com.example.one_vote_service.config.permission.Permission;
import com.example.one_vote_service.config.permission.RequirePermissions;
import com.example.one_vote_service.config.security.AccountDetail;
import com.example.one_vote_service.domain.dto.auth.DetailGroupDto;
import com.example.one_vote_service.domain.dto.auth.GroupDto;
import com.example.one_vote_service.domain.dto.auth.GroupPageFeatureDto;
import com.example.one_vote_service.domain.reponse.LoginResponse;
import com.example.one_vote_service.domain.request.auth.*;
import com.example.one_vote_service.service.AuthService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Slf4j
@Api(value = "API Tài khoản", authorizations = @Authorization(value = "apiKey"))
@RestController
@RequestMapping("auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    @ApiOperation(value = "Tạo tài khoản")
    public ServiceResponseMessage register(
            @RequestBody @Valid RegisterRequest registerRequest
    ) {
        return authService.register(registerRequest);
    }

    @PostMapping("/login")
    @ApiOperation(value = "Đăng nhập")
    @RequirePermissions(Permission.FT_ADD)
    public LoginResponse login(
            @RequestHeader(value = "channel") Channel channel,
            @RequestBody @Valid LoginRequest loginRequest
    ) {
        return null;
//        return authService.login(loginRequest, channel);
    }

    @PostMapping("/logout")
    @ApiOperation(value = "Đăng xuất")
    public ServiceResponseMessage logout(
            @RequestHeader(value = "channel") Channel channel,
            @AuthenticationPrincipal AccountDetail accountDetail
    ) {
        return authService.logout(accountDetail, channel);
    }

    @PostMapping("/refresh-token")
    @ApiOperation(value = "Refresh Token")
    public ResponseEntity<LoginResponse> refreshToken(
            @RequestHeader(value = "channel") Channel channel,
            @RequestBody @Valid RefreshTokenRequest refreshTokenRequest
    ) {
        return authService.refreshToken(channel, refreshTokenRequest);
    }

    @PutMapping("forget-pass")
    @ApiOperation(value = "Quên mật khẩu")
    public ServiceResponseMessage forgetPass(
            @NotNull @Valid @RequestBody UserForgetPassRequest userForgetPassRequest
    ) {
        return authService.forgetPass(userForgetPassRequest);
    }

    @PutMapping("change-pass")
    @ApiOperation(value = "Đổi mật khẩu")
    public ServiceResponseMessage changePass(
            @NotNull @Valid @RequestBody UserChangePassRequest userChangePassRequest,
            @AuthenticationPrincipal AccountDetail accountDetail
    ) {
        return authService.changePassword(userChangePassRequest, accountDetail);
    }

    @PutMapping("reset-pass/{userId}")
    public ServiceResponseMessage resetPassword(
            @NotNull @PathVariable(value = "userId") Long userId
    ) {
        return authService.resetPassword(userId);
    }

    @GetMapping("/user-page-features")
    @ApiOperation(value = "Quyền của người dùng")
    public GroupPageFeatureDto getPageRole(
            @RequestHeader(value = "channel") Channel channel,
            @AuthenticationPrincipal AccountDetail accountDetail
    ) {
        return authService.pageFeature(channel, accountDetail);
    }

    @GetMapping("/user-groups")
    @ApiOperation(value = "Danh sách nhóm người dùng")
    public List<GroupDto> getListGroup(
            @RequestHeader(value = "channel") Channel channel
    ) {
        return authService.getListGroup(channel);
    }

    @PostMapping("/user-groups")
    @ApiOperation(value = "Thêm nhóm người dùng")
    public ServiceResponseMessage addGroup(
            @RequestHeader(value = "channel") Channel channel,
            @RequestBody AddGroupRequest addGroupRequest
    ) {
        return authService.addGroup(channel, addGroupRequest);
    }

    @PutMapping("/user-groups/{groupId}")
    @ApiOperation(value = "Danh sách nhóm người dùng")
    public ServiceResponseMessage updateGroup(
            @RequestHeader(value = "channel") Channel channel,
            @RequestBody UpdateGroupRequest updateGroupRequest,
            @NotNull @PathVariable(value = "groupId") Long groupId
    ) {
        return authService.updateGroup(channel, updateGroupRequest, groupId);
    }

    @DeleteMapping("/user-groups/{groupId}")
    @ApiOperation(value = "Danh sách nhóm người dùng")
    public ServiceResponseMessage deleteGroup(
            @NotNull @PathVariable(value = "groupId") Long groupId
    ) {
        return authService.deleteGroup(groupId);
    }

    @GetMapping("/user-groups/{groupId}")
    @ApiOperation(value = "Chi tiết nhóm người dùng")
    public List<DetailGroupDto> getDetailGroup(
            @RequestHeader(value = "channel") Channel channel,
            @NotNull @PathVariable(value = "groupId") Long groupId
    ) {
        return authService.getDetailGroup(channel, groupId);
    }


}
