package com.ssaw.ssawauthenticatecenterservice.listen;

import com.ssaw.ssawauthenticatecenterfeign.event.UploadScopeFinishedEvent;
import com.ssaw.ssawauthenticatecenterservice.service.ScopeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;

/**
 * @author HuSen
 * @date 2019/2/27 13:45
 */
@Configuration
public class UploadScopeFinishedListener {

    private final ScopeService scopeService;

    @Autowired
    public UploadScopeFinishedListener(ScopeService scopeService) {
        this.scopeService = scopeService;
    }

    @EventListener(UploadScopeFinishedEvent.class)
    public void listen(UploadScopeFinishedEvent uploadScopeFinishedEvent) {
        scopeService.refreshScope((String)uploadScopeFinishedEvent.getSource());
    }
}