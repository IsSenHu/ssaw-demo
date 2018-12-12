package com.ssaw.ssawauthenticatecenterservice.controller;

import com.ssaw.commons.annotations.RequestLog;
import com.ssaw.commons.annotations.Validating;
import com.ssaw.commons.vo.CommonResult;
import com.ssaw.commons.vo.PageReqDto;
import com.ssaw.commons.vo.TableData;
import com.ssaw.ssawauthenticatecenterfeign.dto.ResourceDto;
import com.ssaw.ssawauthenticatecenterservice.service.ResourceService;
import com.ssaw.ssawauthenticatecenterservice.service.impl.ResourceServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

/**
 * @author HuSen.
 * @date 2018/12/12 13:58.
 */
@RestController
@RequestMapping("/api/resource")
public class ResourceController extends BaseController {

    private final ResourceService resourceService;

    @Autowired
    public ResourceController(ResourceServiceImpl resourceService) {
        this.resourceService = resourceService;
    }

    @Validating
    @PostMapping("/add")
    @RequestLog(method = "ResourceController.add(ResourceDto resourceDto)")
    public CommonResult<ResourceDto> add(@RequestBody @Valid ResourceDto resourceDto, BindingResult result) {
        return resourceService.add(resourceDto);
    }

    @PostMapping("/page")
    @RequestLog(method = "ResourceController.page(PageReqDto<ResourceDto> pageReqDto)")
    public TableData<ResourceDto> page(@RequestBody PageReqDto<ResourceDto> pageReqDto) {
        return resourceService.page(pageReqDto);
    }

    @GetMapping("/findById/{id}")
    @RequestLog(method = "ResourceController.findById(Long id)")
    public CommonResult<ResourceDto> findById(@PathVariable(name = "id") Long id) {
        return resourceService.findById(id);
    }

    @Validating
    @PostMapping("/update")
    @RequestLog(method = "ResourceController.update(ResourceDto resourceDto)")
    public CommonResult<ResourceDto> update(@RequestBody @Valid ResourceDto resourceDto, BindingResult result) {
        return resourceService.update(resourceDto);
    }

    @PostMapping("/delete/{id}")
    @RequestLog(method = "ResourceController.delete(Long id)")
    public CommonResult<Long> delete(@PathVariable(name = "id") Long id) {
        return resourceService.delete(id);
    }
}
