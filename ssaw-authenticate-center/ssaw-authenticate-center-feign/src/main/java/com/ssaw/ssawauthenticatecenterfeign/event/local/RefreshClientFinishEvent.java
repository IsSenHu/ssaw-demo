package com.ssaw.ssawauthenticatecenterfeign.event.local;

import org.springframework.context.ApplicationEvent;

/**
 * @author HuSen
 * @date 2019/4/26 18:13
 */
public class RefreshClientFinishEvent extends ApplicationEvent {
    private static final long serialVersionUID = 2819501333890291337L;

    /**
     * Create a new ApplicationEvent.
     *
     * @param source the object on which the event initially occurred (never {@code null})
     */
    public RefreshClientFinishEvent(Object source) {
        super(source);
    }
}