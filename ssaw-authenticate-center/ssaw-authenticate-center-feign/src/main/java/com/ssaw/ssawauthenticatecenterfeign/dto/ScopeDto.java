package com.ssaw.ssawauthenticatecenterfeign.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author HuSen.
 * @date 2018/12/12 11:34.
 */
@Setter
@Getter
public class ScopeDto implements Serializable {

    private Long id;

    @NotBlank(message = "作用域不能为空!")
    @Length(max = 50, message = "作用域最大长度不能超过50!")
    private String scope;

    @NotBlank(message = "uri不能为空!")
    @Length(max = 50, message = "接口地址最大长度不能超过50!")
    private String uri;

    @NotNull(message = "所属资源不能为空!")
    private Long resourceId;

    private String resourceName;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime modifyTime;

    private String createMan;

    private String modifyMan;
}
