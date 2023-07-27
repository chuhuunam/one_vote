package com.example.one_vote_service.domain.dto.auth;

import com.example.one_vote_service.domain.entity.auth.Page;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PageDto {

    private Long id;

    private String code;

    private String name;

    private String icon;

    private String url;

    private String pageIndex;

    private Long parentId;

    private boolean statusPage;

    private List<PageDto> children = new ArrayList<>();

    public PageDto(Page page) {
        this.id = page.getId();
        this.code = page.getCode();
        this.name = page.getName();
        this.icon = page.getIcon();
        this.pageIndex = page.getPageIndex();
        this.url = page.getUrl();
        this.parentId = page.getParentId();
    }

    public PageDto(Page page, boolean statusPage) {
        this.id = page.getId();
        this.code = page.getCode();
        this.name = page.getName();
        this.icon = page.getIcon();
        this.pageIndex = page.getPageIndex();
        this.url = page.getUrl();
        this.parentId = page.getParentId();
        this.statusPage = statusPage;
    }
}
