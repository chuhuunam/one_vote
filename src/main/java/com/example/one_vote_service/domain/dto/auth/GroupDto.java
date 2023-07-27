package com.example.one_vote_service.domain.dto.auth;

import com.example.one_vote_service.domain.entity.auth.Group;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class GroupDto {

    private Long id;
    private String code;
    private String name;
    private String description;
    private boolean status;

    public GroupDto(Group groups) {
        this.id = groups.getId();
        this.code = groups.getCode();
        this.name = groups.getName();
        this.description = groups.getDescription();
        this.status = groups.isStatus();
    }

}
