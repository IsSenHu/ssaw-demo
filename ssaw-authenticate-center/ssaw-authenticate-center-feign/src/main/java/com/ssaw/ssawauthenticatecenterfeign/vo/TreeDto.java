package com.ssaw.ssawauthenticatecenterfeign.vo;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * @author HuSen.
 * @date 2019/1/23 13:46.
 */
@Getter
@Setter
public class TreeDto implements Serializable {

    private Long id;

    private String label;

    private List<TreeDto> children;
}
