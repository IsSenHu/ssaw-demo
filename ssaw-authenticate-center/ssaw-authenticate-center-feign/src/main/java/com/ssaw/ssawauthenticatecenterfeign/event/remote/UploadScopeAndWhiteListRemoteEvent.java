package com.ssaw.ssawauthenticatecenterfeign.event.remote;

import com.ssaw.ssawauthenticatecenterfeign.vo.scope.ScopeVO;
import lombok.Getter;
import lombok.Setter;
import org.springframework.cloud.bus.event.RemoteApplicationEvent;

import java.util.List;

/**
 * 上传作用域和白名单远程事件
 * @author HuSen
 * @date 2019/3/1 18:29
 */
@Getter
@Setter
public class UploadScopeAndWhiteListRemoteEvent extends RemoteApplicationEvent {
    private static final long serialVersionUID = -4395761134307387025L;

    private String resourceId;
    private List<ScopeVO> scopeVOS;
    private List<String> whiteList;

    public UploadScopeAndWhiteListRemoteEvent() {}

    public UploadScopeAndWhiteListRemoteEvent(Object source, String originService, String destinationService, String resourceId, List<ScopeVO> scopeVOS, List<String> whiteList) {
        super(source, originService, destinationService);
        this.resourceId = resourceId;
        this.scopeVOS = scopeVOS;
        this.whiteList = whiteList;
    }
}