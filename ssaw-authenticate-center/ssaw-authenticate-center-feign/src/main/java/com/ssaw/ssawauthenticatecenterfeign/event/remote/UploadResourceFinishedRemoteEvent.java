package com.ssaw.ssawauthenticatecenterfeign.event.remote;

import com.ssaw.ssawauthenticatecenterfeign.vo.resource.UploadResourceVO;
import lombok.Getter;
import lombok.Setter;
import org.springframework.cloud.bus.event.RemoteApplicationEvent;

/**
 * 上传资源完成远程事件
 * @author HuSen
 * @date 2019/3/1 18:08
 */
@Getter
@Setter
public class UploadResourceFinishedRemoteEvent extends RemoteApplicationEvent {

    private static final long serialVersionUID = -4441271171467042241L;

    private Boolean success;

    private UploadResourceVO uploadResourceVO;

    public UploadResourceFinishedRemoteEvent() {}

    public UploadResourceFinishedRemoteEvent(Object source, String originService, String destinationService, Boolean success, UploadResourceVO uploadResourceVO) {
        super(source, originService, destinationService);
        this.success = success;
        this.uploadResourceVO = uploadResourceVO;
    }
}