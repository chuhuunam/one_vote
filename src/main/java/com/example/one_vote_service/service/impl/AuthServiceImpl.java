package com.example.one_vote_service.service.impl;

import com.example.one_vote_service.common.ServiceResponseMessage;
import com.example.one_vote_service.config.constant.Channel;
import com.example.one_vote_service.config.constant.MessageCode;
import com.example.one_vote_service.config.constant.UserType;
import com.example.one_vote_service.config.security.AccountDetail;
import com.example.one_vote_service.config.security.JwtTokenProvider;
import com.example.one_vote_service.domain.dto.auth.*;
import com.example.one_vote_service.domain.entity.auth.*;
import com.example.one_vote_service.domain.reponse.LoginResponse;
import com.example.one_vote_service.domain.request.auth.*;
import com.example.one_vote_service.exception.CustomException;
import com.example.one_vote_service.exception.TokenRefreshException;
import com.example.one_vote_service.repository.*;
import com.example.one_vote_service.service.AuthService;
import com.example.one_vote_service.service.MailService;
import com.example.one_vote_service.service.RefreshTokenService;
import com.example.one_vote_service.utils.RandomUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final UserFcmKeyRepository userFcmKeyRepository;
    private final TokenRepository tokenRepository;
    private final GroupRepository groupRepository;
    private final PageRepository pageRepository;
    BCryptPasswordEncoder _passwordEncoder = new BCryptPasswordEncoder();
    private final JwtTokenProvider jwtService;
    private final RefreshTokenService refreshTokenService;
    private final MailService mailService;

    public AuthServiceImpl(UserRepository userRepository, UserFcmKeyRepository userFcmKeyRepository, TokenRepository tokenRepository, GroupRepository groupRepository, PageRepository pageRepository, JwtTokenProvider jwtService, RefreshTokenService refreshTokenService, MailService mailService) {
        this.userRepository = userRepository;
        this.userFcmKeyRepository = userFcmKeyRepository;
        this.tokenRepository = tokenRepository;
        this.groupRepository = groupRepository;
        this.pageRepository = pageRepository;
        this.jwtService = jwtService;
        this.refreshTokenService = refreshTokenService;
        this.mailService = mailService;
    }

    @Override
    public AccountDetail findById(Integer userId, String jwt) {
        // TODO: Check user có tồn tại hay không
        User user = userRepository.findByIdAndStatusTrue(Long.valueOf(userId));
        // TODO: Check token của user đã login hay chưa
        Token token = tokenRepository.findByTokenAndUserIdAndLogoutFalse(jwt, Long.valueOf(userId));
        if (token == null) {
            throw new TokenRefreshException(MessageCode.REFRESH_TOKEN_NOT_EXIST);
        }
        return user == null || token == null ? null : new AccountDetail(user, jwt);
    }

    @Override
    public ServiceResponseMessage register(RegisterRequest registerRequest) {
        User user = new User();

        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new CustomException(MessageCode.EMAIL_IS_EXIST);
        }

        if (userRepository.existsByUsername(registerRequest.getUsername())) {
            throw new CustomException(MessageCode.USERNAME_IS_EXIST);
        }

        user.setAvatar(registerRequest.getAvatar());
        user.setGender(registerRequest.getGender());
        user.setUsername(registerRequest.getUsername());
        user.setPassword(_passwordEncoder.encode(registerRequest.getPassword()));
        user.setFullName(registerRequest.getFullName());
        user.setEmail(registerRequest.getEmail());
        user.setAvatar(registerRequest.getAvatar());
        user.setPhone(registerRequest.getPhone());
        user.setBirthday(registerRequest.getBirthday());
        user.setStatus(registerRequest.isStatus());
        user.setType(UserType.USER);
        user.setCreateTime(new Date());
        user.setCreateBy(0L);
        userRepository.save(user);

        return new ServiceResponseMessage(MessageCode.REGISTER_USER_SUCCESS);
    }

    @Override
    public LoginResponse login(LoginRequest loginRequest, Channel channel) {

        User user = userRepository.findByUsername(loginRequest.getUsername());
        if (user == null) {
            throw new CustomException(MessageCode.USERNAME_PASSWORD_NOT_EXIST);
        } else if (!user.isStatus()) {
            throw new CustomException(MessageCode.STATUS_USER_FALSE);
        }
        if (!_passwordEncoder.matches(decodeValue(loginRequest.getPassword()), user.getPassword())) {
            throw new CustomException(MessageCode.USERNAME_PASSWORD_NOT_EXIST);
        }

        String token = jwtService.generateToken(user.getId());
        // TODO: Lưu lại token theo channel
        saveTokenIssue(user, token, channel);

        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getId(), channel);

        // TODO: Lưu lại fcmKey firebase
        if (loginRequest.getFcmKey() != null && !loginRequest.getFcmKey().equals("")) {
            saveFcmKey(user, loginRequest.getFcmKey(), channel, token);
        }

        return new LoginResponse(token, refreshToken.getToken());
    }

    private void saveFcmKey(User user, String fcmKey, Channel channel, String token) {

        List<UserFcmKey> userFcmKeys = userFcmKeyRepository.findAllByFcmKey(fcmKey);
        if (!userFcmKeys.isEmpty()) {
            userFcmKeyRepository.deleteAll(userFcmKeys);
        }
        userFcmKeyRepository.save(new UserFcmKey(user.getId(), fcmKey, channel, token));
    }

    private void saveTokenIssue(User user, String token, Channel channel) {
        Token tokenEntity = new Token();
        tokenEntity.setToken(token);
        tokenEntity.setUserId(user.getId());
        tokenEntity.setLogout(false);
        tokenEntity.setChannel(channel);
        tokenEntity.setIssuedAt(jwtService.getIat(token));
        tokenRepository.save(tokenEntity);
    }

    @Override
    public ResponseEntity<LoginResponse> refreshToken(Channel channel, RefreshTokenRequest request) {
        String requestRefreshToken = request.getRefreshToken();

        return refreshTokenService.findByToken(requestRefreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    String token = jwtService.generateToken(user.getId());
                    // TODO: Lưu lại fcmKey firebase
                    if (request.getFcmKey() != null && !request.getFcmKey().equals("")) {
                        saveFcmKey(user, request.getFcmKey(), channel, token);
                    }
                    return ResponseEntity.ok(new LoginResponse(token, refreshTokenService.createRefreshToken(user.getId(), channel).getToken()));
                })
                .orElseThrow(() -> new TokenRefreshException(MessageCode.REFRESH_TOKEN_NOT_EXIST));
    }

    @Override
    public ServiceResponseMessage logout(AccountDetail accountDetail, Channel channel) {
        Token token = tokenRepository.findByTokenAndUserIdAndChannelAndLogoutFalse(accountDetail.getJwt(), accountDetail.getId(), channel);
        if (token != null) {
            token.setLogout(true);
            tokenRepository.save(token);
        }
        refreshTokenService.deleteByUserId(accountDetail.getId(), channel);
        UserFcmKey userFcmKey = userFcmKeyRepository.findByUserIdAndChannelAndToken(accountDetail.getId(), channel, accountDetail.getJwt());
        if (userFcmKey != null) {
            userFcmKeyRepository.delete(userFcmKey);
        }

        return new ServiceResponseMessage(MessageCode.LOGOUT_SUCCESS);
    }

    @Override
    public GroupPageFeatureDto pageFeature(Channel channel, AccountDetail accountDetail) {
        User user = userRepository.findById(accountDetail.getId())
                .orElseThrow(() -> new CustomException(MessageCode.USER_NOT_EXIST));
        List<PageDto> pages = new ArrayList<>();
        List<FeatureDto> features = new ArrayList<>();

        user.getUserGroups().forEach(userGroup -> userGroup.getGroup().getGroupPageFeatures().forEach(groupPageFeature -> {
            if (groupPageFeature.isStatusPage() && groupPageFeature.isStatusFeature()) {
                if (groupPageFeature.getPage().isStatus()) {
                    pages.add(new PageDto(groupPageFeature.getPage()));
                }
                if (groupPageFeature.getFeature().isStatus()) {
                    features.add(new FeatureDto(groupPageFeature.getFeature()));
                }
            }
        }));
        Set<PageDto> setPages = new HashSet<>(pages);
        Set<FeatureDto> setFeatures = new HashSet<>(features);
        return new GroupPageFeatureDto(user, new ArrayList<>(setPages), new ArrayList<>(setFeatures));
    }

    @Override
    public List<GroupDto> getListGroup(Channel channel) {
        return groupRepository.findAllByStatusTrue().stream().map(GroupDto::new).collect(Collectors.toList());
    }

    @Override
    public List<DetailGroupDto> getDetailGroup(Channel channel, Long groupId) {
        List<DetailGroupDto> detailGroupDtoList = new ArrayList<>();

        Set<PageDto> pages = new HashSet<>();
        List<Long> pageIds = new ArrayList<>();
        Set<FeatureDto> features = new HashSet<>();

        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new CustomException(MessageCode.GROUP_NOT_EXIST));

        group.getGroupPageFeatures().forEach(groupPageFeature -> {
            if (groupPageFeature.getPage().isStatus()) {
                pages.add(new PageDto(groupPageFeature.getPage(), groupPageFeature.isStatusPage()));
                pageIds.add(groupPageFeature.getPage().getId());
            }
            if (groupPageFeature.getFeature().isStatus()) {
                features.add(new FeatureDto(groupPageFeature.getFeature(), groupPageFeature.isStatusFeature()));
            }
        });

        List<Page> pageRemain;
        if (pageIds.isEmpty()) {
            pageRemain = pageRepository.findAllByStatusTrue();
        } else {
            pageRemain = pageRepository.findAllByStatusTrueAndIdNotIn(pageIds);
        }
        pageRemain.forEach(page -> {
            pages.add(new PageDto(page, false));
            features.addAll(page.getFeatures().stream().map(feature -> new FeatureDto(feature, false)).collect(Collectors.toList()));
        });
        List<PageDto> pageList = new ArrayList<>(pages);
        List<FeatureDto> featureList = new ArrayList<>(features);
        pageList.sort(Comparator.comparingLong(PageDto::getParentId));
        Map<Long, DetailGroupDto> hashMapPages = new HashMap<>();
        for (PageDto node : pages) {
            if (node != null) {
                DetailGroupDto dto = new DetailGroupDto();
                dto.setId(node.getId());
                dto.setCode(node.getCode());
                dto.setName(node.getName());
                dto.setStatusPage(node.isStatusPage());
                dto.setFeatures(featureList.stream().filter(feature -> feature.getPageId().equals(node.getId()))
                        .collect(Collectors.toList()));
                if (!hashMapPages.containsKey(node.getParentId())) {
                    hashMapPages.put(node.getId(), dto);
                } else {
                    hashMapPages.get(node.getParentId()).getChildren().add(dto);
                }
            }
        }
        if (!hashMapPages.isEmpty()) {
            hashMapPages.keySet().forEach(e ->
                    detailGroupDtoList.add(hashMapPages.get(e))
            );
        }
        return detailGroupDtoList;
    }

    @Override
    public ServiceResponseMessage addGroup(Channel channel, AddGroupRequest addGroupRequest) {

        Group group = new Group();
        group.setCode(addGroupRequest.getCode());
        group.setName(addGroupRequest.getName());
        group.setDescription(addGroupRequest.getDescription());
        group.setStatus(addGroupRequest.isStatus());
        groupRepository.save(group);

        return new ServiceResponseMessage(MessageCode.ADD_USER_GROUP_SUCCESS);
    }

    @Override
    public ServiceResponseMessage updateGroup(Channel channel, UpdateGroupRequest updateGroupRequest, Long groupId) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new CustomException(MessageCode.GROUP_NOT_EXIST));
        group.setCode(updateGroupRequest.getCode());
        group.setName(updateGroupRequest.getName());
        group.setDescription(updateGroupRequest.getDescription());
        group.setStatus(updateGroupRequest.isStatus());
        groupRepository.save(group);


        return new ServiceResponseMessage(MessageCode.UPDATE_USER_GROUP_SUCCESS);
    }

    @Override
    public ServiceResponseMessage deleteGroup(Long groupId) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new CustomException(MessageCode.GROUP_NOT_EXIST));
        groupRepository.delete(group);

        return new ServiceResponseMessage(MessageCode.DELETE_USER_GROUP_SUCCESS);
    }

    @Override
    public ServiceResponseMessage changePassword(UserChangePassRequest userChangePassRequest, AccountDetail accountDetail) {

        User user = userRepository.findById(accountDetail.getId())
                .orElseThrow(() -> new CustomException(MessageCode.USER_NOT_EXIST));

        if (!_passwordEncoder.matches(decodeValue(userChangePassRequest.getOldPassword()), user.getPassword())) {
            throw new CustomException(MessageCode.PASSWORD_OLD_NOT_EXIST);
        }

        user.setPassword(_passwordEncoder.encode(userChangePassRequest.getNewPassword()));
        userRepository.save(user);
        return new ServiceResponseMessage(MessageCode.CHANGE_PASSWORD_SUCCESS);
    }

    @Override
    public ServiceResponseMessage resetPassword(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(MessageCode.USER_NOT_EXIST));
        user.setPassword(_passwordEncoder.encode("123@123a"));
        userRepository.save(user);
        return new ServiceResponseMessage(MessageCode.RESET_PASSWORD_SUCCESS);
    }

    @Override
    public ServiceResponseMessage forgetPass(UserForgetPassRequest userForgetPassRequest) {

        User user = userRepository.findByEmail(userForgetPassRequest.getEmail());
        if (user != null) {
            try {
                String password = RandomUtils.generateRandomPassword();
                user.setPassword(_passwordEncoder.encode(password));
                mailService.SendMail(user, password);
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        }
        return new ServiceResponseMessage(MessageCode.SEND_EMAIL_PASSWORD_SUCCESS);
    }

    // Decodes a URL encoded string using `UTF-8`
    public static String decodeValue(String value) {
        try {
            return URLDecoder.decode(value, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException ex) {
            throw new RuntimeException(ex.getCause());
        }
    }
}
