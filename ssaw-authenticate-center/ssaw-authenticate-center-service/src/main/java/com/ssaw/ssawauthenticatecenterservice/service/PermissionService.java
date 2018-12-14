package com.ssaw.ssawauthenticatecenterservice.service;

import com.ssaw.commons.vo.CommonResult;
import com.ssaw.commons.vo.PageReqDto;
import com.ssaw.commons.vo.TableData;
import com.ssaw.ssawauthenticatecenterfeign.dto.PermissionDto;

/**
 * @author HuSen.
 * @date 2018/12/13 17:04.
 */
public interface PermissionService {
    CommonResult<PermissionDto> add(PermissionDto permissionDto);

    TableData<PermissionDto> page(PageReqDto<PermissionDto> pageReqDto);

    CommonResult<Long> delete(Long id);
}
