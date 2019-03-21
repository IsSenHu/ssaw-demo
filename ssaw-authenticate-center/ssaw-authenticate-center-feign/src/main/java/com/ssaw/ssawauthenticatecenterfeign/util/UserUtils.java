package com.ssaw.ssawauthenticatecenterfeign.util;

import com.ssaw.ssawauthenticatecenterfeign.vo.user.SimpleUserAttributeVO;
import org.springframework.util.Assert;

/**
 * @author HuSen
 * @date 2019/3/1 9:52
 */
public class UserUtils {

    private static final ThreadLocal<SimpleUserAttributeVO> USER = new InheritableThreadLocal<>();

    public static void clearUser() {
        USER.remove();
    }

    public static SimpleUserAttributeVO getUser() {
        SimpleUserAttributeVO simpleUserAttributeVO = USER.get();
        if (simpleUserAttributeVO == null) {
            simpleUserAttributeVO = createEmptyUser();
            USER.set(simpleUserAttributeVO);
        }
        return simpleUserAttributeVO;
    }

    public static void setUser(SimpleUserAttributeVO simpleUserAttributeVO) {
        Assert.notNull(simpleUserAttributeVO, "Only non-null SimpleUserAttributeVO instances are permitted");
        USER.set(simpleUserAttributeVO);
    }

    private static SimpleUserAttributeVO createEmptyUser() {
        return new SimpleUserAttributeVO();
    }
}