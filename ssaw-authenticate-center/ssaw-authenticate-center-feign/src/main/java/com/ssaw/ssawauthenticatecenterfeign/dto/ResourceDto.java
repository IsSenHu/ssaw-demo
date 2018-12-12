package com.ssaw.ssawauthenticatecenterfeign.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author HuSen.
 * @date 2018/12/11 14:08.
 */
@Setter
@Getter
public class ResourceDto implements Serializable {
    private Long id;

    @NotBlank(message = "资源ID不能为空!")
    @Length(max = 50, message = "资源的最大长度不能超过50!")
    private String resourceId;

    private String description;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime modifyTime;

    private String createMan;

    private String modifyMan;
}
