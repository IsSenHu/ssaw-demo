package com.ssaw.ssawservicedemo.service;

import com.ssaw.ssawservicedemo.entity.User;

import java.util.List;
import java.util.Map;

/**
 * @author HuSen.
 * @date 2018/11/12 0:06.
 */
public interface DemoService {
    Map<String,String> get();

    List<User> users();
}
