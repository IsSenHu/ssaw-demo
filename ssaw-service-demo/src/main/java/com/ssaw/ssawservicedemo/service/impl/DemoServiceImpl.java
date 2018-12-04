package com.ssaw.ssawservicedemo.service.impl;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.ssaw.ssawservicedemo.dao.UserMapper;
import com.ssaw.ssawservicedemo.entity.User;
import com.ssaw.ssawservicedemo.service.DemoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author HuSen.
 * @date 2018/11/12 0:07.
 */
@Service
@Slf4j
public class DemoServiceImpl implements DemoService {

    private final UserMapper userMapper;

    @Autowired
    public DemoServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    @HystrixCommand(fallbackMethod = "getFallBack", commandKey = "demoKey")
    public Map<String, String> get() {
        Map<String, String> result = new HashMap<>(1);
        result.put("key", "demo");
        return result;
    }

    @Override
    public List<User> users() {
        return userMapper.selectList(null);
    }

    @SuppressWarnings("unused")
    public Map<String, String> getFallBack() {
        Map<String, String> result = new HashMap<>(1);
        result.put("key", "demoFallBack");
        return result;
    }
}
