package com.ssaw.ssawauthenticatecenterservice.controller;

import com.ssaw.ssawauthenticatecenterfeign.dto.AuthenticationDto;
import com.ssaw.ssawauthenticatecenterfeign.dto.AuthenticationResultDto;
import com.ssaw.ssawauthenticatecenterservice.service.RemoteTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author HuSen.
 * @date 2019/1/22 17:51.
 */
@RestController
@RequestMapping("/api/oauth2")
public class RemoteTokenController extends BaseController {

    private final RemoteTokenService remoteTokenService;

    @Autowired
    public RemoteTokenController(RemoteTokenService remoteTokenService) {
        this.remoteTokenService = remoteTokenService;
    }

    @PostMapping("/authenticate")
    public AuthenticationResultDto authenticate(@RequestBody AuthenticationDto authentication) {
        return remoteTokenService.authenticate(authentication);
    }
}
