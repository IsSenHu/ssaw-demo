package com.ssaw.ssawauthenticatecenterfeign.event;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

/**
 * @author HuSen
 * @date 2019/2/27 13:38
 */
@Getter
@Setter
public class UploadScopeFinishedEvent extends ApplicationEvent {
    public UploadScopeFinishedEvent(Object source) {
        super(source);
    }
}