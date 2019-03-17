package com.ssaw.ssawmehelper.service.consumption.impl;

import com.ssaw.ssawmehelper.dao.mapper.consumption.MyConsumptionMapper;
import com.ssaw.ssawmehelper.service.consumption.MyConsumptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author HuSen
 * @date 2019/3/16 13:24
 */
@Service
public class MyConsumptionServiceImpl implements MyConsumptionService {

    private final MyConsumptionMapper myConsumptionMapper;

    @Autowired
    public MyConsumptionServiceImpl(MyConsumptionMapper myConsumptionMapper) {
        this.myConsumptionMapper = myConsumptionMapper;
    }
}