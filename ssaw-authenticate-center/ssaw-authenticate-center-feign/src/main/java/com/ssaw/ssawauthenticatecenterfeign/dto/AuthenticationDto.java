package com.ssaw.ssawauthenticatecenterfeign.dto;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

/**
 * @author HuSen.
 * @date 2019/1/23 10:54.
 */
@Setter
@Getter
public class AuthenticationDto implements Serializable {
    private String tokenValue;
    private String uri;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
