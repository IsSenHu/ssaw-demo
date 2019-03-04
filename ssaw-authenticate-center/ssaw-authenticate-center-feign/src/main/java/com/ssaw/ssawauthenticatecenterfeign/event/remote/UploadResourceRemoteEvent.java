package com.ssaw.ssawauthenticatecenterfeign.event.remote;

import com.ssaw.ssawauthenticatecenterfeign.vo.resource.UploadResourceVO;
import lombok.Getter;
import lombok.Setter;
import org.springframework.cloud.bus.event.RemoteApplicationEvent;

/**
 * 上传资源服务远程事件
 * @author HuSen
 * @date 2019/3/1 18:05
 */
@Getter
@Setter
public class UploadResourceRemoteEvent extends RemoteApplicationEvent {
    private static final long serialVersionUID = -1788552518892377316L;

    private UploadResourceVO uploadResourceVO;

    public UploadResourceRemoteEvent() {}

    public UploadResourceRemoteEvent(Object source, String originService, String destinationService, UploadResourceVO uploadResourceVO) {
        super(source, originService, destinationService);
        this.uploadResourceVO = uploadResourceVO;
    }
}