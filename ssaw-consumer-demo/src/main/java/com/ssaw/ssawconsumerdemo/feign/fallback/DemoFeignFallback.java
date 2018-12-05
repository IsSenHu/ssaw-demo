package com.ssaw.ssawconsumerdemo.feign.fallback;

import com.ssaw.commons.vo.CommonResult;
import com.ssaw.ssawconsumerdemo.feign.DemoFeign;
import com.ssaw.ssawuserresourcefeign.dto.UserDto;
import org.springframework.stereotype.Component;
import java.util.HashMap;
import java.util.Map;

/**
 * @author HuSen.
 * @date 2018/11/14 11:31.
 */
public class DemoFeignFallback implements DemoFeign {
    @Override
    public Map get() {
        return new HashMap(0);
    }

    @Override
    public CommonResult<UserDto> findByUsername(String username) {
        return null;
    }
}
