package com.ssaw.ssawauthenticatecenterfeign.vo;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * @author hszyp
 * @date 2019/01/27
 */
@Setter
@Getter
public class ResourceScopeDto implements Serializable {
    private ResourceDto resource;
    private List<ScopeDto> scopes;
}
