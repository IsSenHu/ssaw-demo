package com.ssaw.ssawservicedemo.controller;

import com.ssaw.ssawservicedemo.entity.User;
import com.ssaw.ssawservicedemo.service.DemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.Map;

/**
 * @author HuSen.
 * @date 2018/11/12 0:06.
 */
@RestController
@RequestMapping("/demo")
public class DemoController {

    private final DemoService demoService;

    @Autowired
    public DemoController(DemoService demoService) {
        this.demoService = demoService;
    }

    @GetMapping("/get")
    public Map<String, String> get() {
        return demoService.get();
    }

    @GetMapping("/users")
    public List<User> users() {
        return demoService.users();
    }
}
