package com.ssaw.ssawauthenticatecenterservice.endpoint;

import org.springframework.security.oauth2.provider.endpoint.FrameworkEndpoint;
import org.springframework.web.bind.annotation.SessionAttributes;

/**
 * @author HuSen
 * @date 2019/4/29 2:10
 */
@FrameworkEndpoint
@SessionAttributes("authorizationRequest")
public class MyApprovalEndpoint {

}