package com.ssaw.ssawauthenticatecenterfeign.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.*;

/**
 * @author HuSen
 * @date 2019/2/27 9:30
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoDto implements Serializable {
    /** ID */
    private Long id;
    /** 用户名 */
    private String username;
    /** 权限 */
    private Set<String> permissions = new HashSet<>();
    /** 菜单 */
    private List<Menu> menus = new ArrayList<>();
    /** Token */
    private String token;
    /** 自定义信息 */
    private Map<String, Object> info = new HashMap<>();
    /** 按钮 */
    private List<Button> buttons = new ArrayList<>();
}