package com.ssaw.ssawauthenticatecenterservice.interceptor;

import com.alibaba.fastjson.JSON;
import com.ssaw.ssawauthenticatecenterfeign.vo.user.SimpleUserAttributeVO;
import com.ssaw.ssawauthenticatecenterservice.util.UserUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

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
}