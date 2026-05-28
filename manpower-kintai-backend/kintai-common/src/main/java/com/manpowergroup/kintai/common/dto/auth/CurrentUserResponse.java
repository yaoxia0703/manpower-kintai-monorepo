package com.manpowergroup.kintai.common.dto.auth;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class CurrentUserResponse {

    private UserProfile user;

    private List<String> roles;

    private List<String> permissions;

    private List<MenuItem> menus;

    @Data
    @Builder
    public static class UserProfile {
        private Long employeeId;
        private Long accountId;
        private Long companyId;
        private String employeeCode;
        private String displayName;
        private String email;
    }

    @Data
    @Builder
    public static class MenuItem {
        private Long id;
        private Long parentId;
        private String name;
        private String code;
        private String path;
        private String component;
        private String icon;
        private Integer type;
        private Integer sort;
        private Integer visible;
    }
}
