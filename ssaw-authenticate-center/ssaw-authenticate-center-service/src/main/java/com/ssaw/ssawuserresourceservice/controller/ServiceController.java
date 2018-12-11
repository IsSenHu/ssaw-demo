package com.ssaw.ssawuserresourceservice.controller;

import com.ssaw.commons.annotations.RequestLog;
import com.ssaw.commons.annotations.Validating;
import com.ssaw.commons.vo.CommonResult;
import com.ssaw.ssawuserresourcefeign.dto.ServiceDto;
import com.ssaw.ssawuserresourceservice.service.ServiceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

/**
 * @author HuSen.
 * @date 2018/12/11 11:56.
 */
@Slf4j
@RestController
@RequestMapping("/api/service")
public class ServiceController extends BaseController {

    private final ServiceService serviceService;

    @Autowired
    public ServiceController(ServiceService serviceService) {
        this.serviceService = serviceService;
    }

    @RequestLog(method = "查询服务>>>[findByServiceName(String name)]")
    @GetMapping("/get/{name}")
    public CommonResult<ServiceDto> findByServiceName(@PathVariable(name = "name") String name) {
        return serviceService.findByServiceName(name);
    }

    @Validating
    @RequestLog(method = "新增或修改服务信息>>>[save(ServiceDto serviceDto)]")
    @PostMapping("/save")
    public CommonResult<ServiceDto> save(@RequestBody @Valid ServiceDto serviceDto, BindingResult result) {
        return serviceService.save(serviceDto);
    }
}
