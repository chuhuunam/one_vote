package com.example.one_vote_service.domain.dto.auth;

import com.example.one_vote_service.domain.entity.auth.Group;
import com.example.one_vote_service.domain.entity.auth.User;
import com.example.one_vote_service.domain.entity.auth.UserGroup;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupPageFeatureDto {

    private List<GroupDto> groups;
    private PageFeatureDto pageFeature;

    public GroupPageFeatureDto(User user, List<PageDto> pages, List<FeatureDto> features) {

        PageFeatureDto pageFeatureDto = new PageFeatureDto();

        List<GroupDto> groups = user.getUserGroups()
                .stream()
                .map(UserGroup::getGroup)
                .filter(Group::isStatus)
                .map(GroupDto::new)
                .collect(Collectors.toList());

        pages.sort(Comparator.comparingLong(PageDto::getParentId));

        Map<Long, PageDto> hashMapPages = new HashMap<>();
        for (PageDto node : pages) {
            if (node != null) {
                if (!hashMapPages.containsKey(node.getParentId())) {
                    hashMapPages.put(node.getId(), node);
                } else {
                    hashMapPages.get(node.getParentId()).getChildren().add(node);
                }
            }
        }

        List<PageDto> pageDtoList = new ArrayList<>();
        if (!hashMapPages.isEmpty()) {
            hashMapPages.keySet().forEach(e ->
                    pageDtoList.add(hashMapPages.get(e))
            );
        }

        pageFeatureDto.setPages(pageDtoList);
        pageFeatureDto.setFeatures(features.stream().map(FeatureDto::getCode).collect(Collectors.toList()));
        this.groups = groups;
        this.pageFeature = pageFeatureDto;
    }
}
