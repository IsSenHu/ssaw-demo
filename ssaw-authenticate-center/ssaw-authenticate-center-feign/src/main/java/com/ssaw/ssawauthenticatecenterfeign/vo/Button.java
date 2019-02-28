package com.ssaw.ssawauthenticatecenterfeign.vo;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author HuSen
 * @date 2019/2/27 12:58
 */
@Getter
@Setter
public class Button implements Serializable {
    private String key;
    private String scope;
    private String name;
}