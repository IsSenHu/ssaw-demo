package com.ssaw.ssawauthenticatecenterfeign.vo.scope;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author HuSen.
 * @date 2018/12/12 11:34.
 */
@Data
public class CreateScopeVO implements Serializable {
    private static final long serialVersionUID = -2070902044902750570L;

    @NotBlank(message = "作用域不能为空!")
    @Length(max = 50, message = "作用域最大长度不能超过50!")
    private String scope;

    @NotBlank(message = "uri不能为空!")
    @Length(max = 50, message = "接口地址最大长度不能超过50!")
    private String uri;

    @NotNull(message = "所属资源不能为空!")
    private Long resourceId;

    private String resourceName;
}
