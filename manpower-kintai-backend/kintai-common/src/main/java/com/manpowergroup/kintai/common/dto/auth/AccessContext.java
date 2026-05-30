package com.manpowergroup.kintai.common.dto.auth;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class AccessContext {

    private CurrentUserResponse.UserProfile user;

    private List<String> roles;

    private List<String> permissions;

    private List<String> authorities;

    private List<CurrentUserResponse.MenuItem> menus;
}
