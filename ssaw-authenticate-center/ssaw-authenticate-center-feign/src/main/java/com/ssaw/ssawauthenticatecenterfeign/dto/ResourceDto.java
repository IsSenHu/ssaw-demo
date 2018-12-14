package com.ssaw.ssawauthenticatecenterfeign.dto;

import com.ssaw.commons.dto.BaseDto;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.NotBlank;

/**
 * @author HuSen.
 * @date 2018/12/11 14:08.
 */
@Setter
@Getter
public class ResourceDto extends BaseDto {

    @NotBlank(message = "资源ID不能为空!")
    @Length(max = 50, message = "资源的最大长度不能超过50!")
    private String resourceId;

    private String description;
}
