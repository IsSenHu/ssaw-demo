package com.ssaw.ssawuserresourcefeign.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author HuSen.
 * @date 2018/11/27 18:59.
 */
@Getter
@Setter
public class RoleDto implements Serializable {
    /**
     * 主键
     **/
    private Long id;

    private String url;

    @NotBlank(message = "唯一标识不能为空")
    private String uniqueMark;

    private String description;

    /** 权限类型 */
    private Integer type;
    /** 是否父节点 */
    private Boolean isParent;
    /** 父节点ID */
    private Long parentId;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
