package com.ssaw.ssawuserresourceservice.service;

import com.ssaw.commons.vo.CommonResult;

/**
 * @author HuSen.
 * @date 2018/11/27 19:01.
 */
public class BaseService {

    /**
     * 创建CommonResult
     * @param code 状态码
     * @param message 消息
     * @param data 数据
     * @param <T> 数据类型
     * @return CommonResult
     */
    public <T> CommonResult<T> createResult(Integer code, String message, T data) {
        return CommonResult.createResult(code, message, data);
    }
}
