package com.ssaw.ssawauthenticatecenterfeign.event.remote;

import com.ssaw.ssawauthenticatecenterfeign.vo.ButtonVO;
import com.ssaw.ssawauthenticatecenterfeign.vo.MenuVO;
import lombok.Getter;
import lombok.Setter;
import org.springframework.cloud.bus.event.RemoteApplicationEvent;

import java.util.List;

/**
 * 上传菜单和按钮远程事件
 * @author HuSen
 * @date 2019/3/1 18:01
 */
@Getter
@Setter
public class UploadMenuAndButtonRemoteEvent extends RemoteApplicationEvent {
    private static final long serialVersionUID = -5350117160551700051L;

    private String resourceId;
    private MenuVO menuVO;
    private List<ButtonVO> buttonVOS;

    public UploadMenuAndButtonRemoteEvent() {}

    public UploadMenuAndButtonRemoteEvent(Object source, String originService, String destinationService, String resourceId, MenuVO menuVO, List<ButtonVO> buttonVOS) {
        super(source, originService, destinationService);
        this.resourceId = resourceId;
        this.menuVO = menuVO;
        this.buttonVOS = buttonVOS;
    }
}