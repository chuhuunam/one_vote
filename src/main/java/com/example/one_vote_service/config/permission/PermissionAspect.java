package com.example.one_vote_service.config.permission;

import com.example.one_vote_service.config.constant.MessageCode;
import com.example.one_vote_service.config.security.AccountDetail;
import com.example.one_vote_service.domain.entity.auth.User;
import com.example.one_vote_service.exception.PermissionException;
import com.example.one_vote_service.repository.UserRepository;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Aspect
@Component
public class PermissionAspect {

    private final UserRepository userRepository;

    public PermissionAspect(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Before("@annotation(requirePermissions)")
    public void checkPermissions(RequirePermissions requirePermissions) {
        // TODO: Lấy danh sách quyền yêu cầu từ annotation
        List<String> requiredPermissions = Arrays.stream(requirePermissions.value())
                .map(Enum::toString).collect(Collectors.toList());

        // TODO: Nếu có quyền thì đi check api
        if (!requiredPermissions.isEmpty()) {
            Long userId = ((AccountDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
            List<String> features = this.getListFeatures(userId);

            //TODO: Kiểm tra xem người dùng có đủ quyền yêu cầu hay không
            if (!features.containsAll(requiredPermissions)) {
                throw new PermissionException(MessageCode.USER_NOT_FORBIDDEN);
            }
        }
    }

    // TODO Lấy danh sách quyền lưu cache lại
    @Cacheable("dataCache")
    public List<String> getListFeatures(Long userId) {
        List<String> features = new ArrayList<>();
        User user = userRepository.findById(userId).orElse(null);
        assert user != null;
        user.getUserGroups().forEach(userGroup -> userGroup.getGroup().getGroupPageFeatures().forEach(groupPageFeature -> {
            if (groupPageFeature.isStatusPage() && groupPageFeature.isStatusFeature()) {
                if (groupPageFeature.getFeature().isStatus()) {
                    features.add(groupPageFeature.getFeature().getCode());
                }
            }
        }));
        return features;
    }
}
