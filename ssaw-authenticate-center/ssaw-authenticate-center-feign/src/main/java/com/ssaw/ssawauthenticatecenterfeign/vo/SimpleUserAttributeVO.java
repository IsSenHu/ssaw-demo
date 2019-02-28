package com.ssaw.ssawauthenticatecenterfeign.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.*;

import java.io.Serializable;

/**
 * @author HuSen
 * @date 2019/2/28 17:37
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SimpleUserAttributeVO implements Serializable {
    /** ID */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
    /** 用户名 */
    private String username;
}