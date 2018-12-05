package com.ssaw.ssawconsumerdemo.controller;

import com.ssaw.commons.vo.CommonResult;
import com.ssaw.ssawconsumerdemo.feign.DemoFeign;
import com.ssaw.ssawuserresourcefeign.dto.UserDto;
import com.ssaw.ssawuserresourcefeign.feign.UserFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

/**
 * @author HuSen.
 * @date 2018/11/12 0:37.
 */
@RestController
@RequestMapping("/demo")
public class DemoController {

    private final DemoFeign demoFeign;

    private final UserFeign userFeign;

    @Autowired
    public DemoController(DemoFeign demoFeign, UserFeign userFeign) {
        this.demoFeign = demoFeign;
        this.userFeign = userFeign;
    }

    @GetMapping("/get")
    public Map get() {
        return demoFeign.get();
    }

    @GetMapping("/user/{username}")
    public CommonResult<UserDto> get(@PathVariable(name = "username") String username) {
        return userFeign.findByUsername(username);
    }
}
