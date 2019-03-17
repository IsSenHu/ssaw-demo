package com.ssaw.ssawmehelper.api.consumption;

import com.ssaw.ssawmehelper.api.BaseController;
import com.ssaw.ssawmehelper.service.consumption.MyConsumptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author HuSen
 * @date 2019/3/16 13:24
 */
@RestController
@RequestMapping("/api/consumption")
public class MyConsumptionController extends BaseController {

    private final MyConsumptionService myConsumptionService;

    @Autowired
    public MyConsumptionController(ApplicationContext context, MyConsumptionService myConsumptionService) {
        super(context);
        this.myConsumptionService = myConsumptionService;
    }


}