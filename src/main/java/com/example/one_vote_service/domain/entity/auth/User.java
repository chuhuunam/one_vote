package com.example.one_vote_service.domain.entity.auth;

import com.example.one_vote_service.config.constant.Gender;
import com.example.one_vote_service.config.constant.UserType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Where;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "users") // người dùng
@Where(clause = "is_delete = false")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String avatar;

    private String username;

    private String password;

    private String fullName;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    private UserType type;

    private String phone;

    private String email;

    private Date birthday;

    private boolean status;

    @CreatedDate
    protected Date createTime;

    @LastModifiedDate
    protected Date updateTime;

    protected Long createBy;

    protected Long updateBy;

    @Column(name = "is_delete")
    protected boolean isDelete = false;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonIgnore
    private List<UserGroup> userGroups = new ArrayList<>();
}
