package com.ssaw.ssawconsumerdemo.feign;

import com.ssaw.commons.vo.CommonResult;
import com.ssaw.ssawconsumerdemo.feign.fallback.DemoFeignFallback;
import com.ssaw.ssawuserresourcefeign.dto.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;

/**
 * @author HuSen.
 * @date 2018/11/14 11:30.
 */
@Component
@FeignClient(name = "demo-feign", path = "/api/user", serviceId = "SSAW-USER-SERVER", decode404 = true, fallback = DemoFeignFallback.class)
public interface DemoFeign {

    /**
     * 测试用
     * @return 结果
     */
    @GetMapping("/get")
    Map get();

    @GetMapping("/get/{username}")
    CommonResult<UserDto> findByUsername(@PathVariable(name = "username") String username);
}
