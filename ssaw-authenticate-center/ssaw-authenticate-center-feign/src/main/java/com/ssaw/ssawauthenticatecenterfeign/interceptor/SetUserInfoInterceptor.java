package com.ssaw.ssawauthenticatecenterfeign.interceptor;

import com.alibaba.fastjson.JSON;
import com.ssaw.ssawauthenticatecenterfeign.util.UserUtils;
import com.ssaw.ssawauthenticatecenterfeign.vo.user.SimpleUserAttributeVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author HuSen
 * @date 2019/3/1 9:50
 */
@Slf4j
public class SetUserInfoInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String userInfo = request.getHeader("userInfo");
        if (StringUtils.isNotBlank(userInfo)) {
            try {
                if (log.isDebugEnabled()) {
                    log.debug("设置用户上下文信息:{}", userInfo);
                }
                SimpleUserAttributeVO simpleUserAttributeVO = JSON.parseObject(userInfo, SimpleUserAttributeVO.class);
                UserUtils.setUser(simpleUserAttributeVO);
            } catch (Exception e) {
                log.error("设置用户上下文异常:",  e);
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        UserUtils.clearUser();
    }
}