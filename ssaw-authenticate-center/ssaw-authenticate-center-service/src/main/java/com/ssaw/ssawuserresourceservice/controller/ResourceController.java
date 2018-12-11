package com.ssaw.ssawuserresourceservice.controller;

import com.ssaw.commons.annotations.RequestLog;
import com.ssaw.commons.annotations.Validating;
import com.ssaw.commons.vo.CommonResult;
import com.ssaw.ssawuserresourcefeign.dto.ResourceDto;
import com.ssaw.ssawuserresourceservice.service.ResourceService;
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
@RequestMapping("/api/resource")
public class ResourceController extends BaseController {

    private final ResourceService resourceService;

    @Autowired
    public ResourceController(ResourceService resourceService) {
        this.resourceService = resourceService;
    }

    @RequestLog(method = "查询资源>>>[findByMark(String mark)]")
    @GetMapping("/get/{mark}")
    public CommonResult<ResourceDto> findByMark(@PathVariable(name = "mark") String mark) {
        return resourceService.findByMark(mark);
    }

    @Validating
    @PostMapping("/save")
    @RequestLog(method = "新增或修改资源>>>[save(ResourceDto resourceDto)]")
    public CommonResult<ResourceDto> save(@RequestBody @Valid ResourceDto resourceDto, BindingResult result) {
        return resourceService.save(resourceDto);
    }

    @PostMapping("/bind")
    @RequestLog(method = "绑定资源与服务>>>[bind(Integer resourceId, Integer serviceId)]")
    public CommonResult<String> bind(@RequestParam(name = "resourceId") Integer resourceId, @RequestParam(name = "serviceId") Integer serviceId) {
        return resourceService.bind(resourceId, serviceId);
    }
}
