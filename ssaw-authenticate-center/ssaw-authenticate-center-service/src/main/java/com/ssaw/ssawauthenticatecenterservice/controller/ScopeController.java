package com.ssaw.ssawauthenticatecenterservice.controller;

import com.ssaw.commons.annotations.RequestLog;
import com.ssaw.commons.annotations.Validating;
import com.ssaw.commons.vo.CommonResult;
import com.ssaw.ssawauthenticatecenterfeign.dto.ScopeDto;
import com.ssaw.ssawauthenticatecenterservice.service.ScopeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.validation.Valid;

/**
 * @author HuSen.
 * @date 2018/12/12 11:33.
 */
@RestController
@RequestMapping("/api/scope")
public class ScopeController extends BaseController {

    private final ScopeService scopeService;

    @Autowired
    public ScopeController(ScopeService scopeService) {
        this.scopeService = scopeService;
    }

    @Validating
    @PostMapping("/add")
    @RequestLog(method = "ScopeController.add(ScopeDto scopeDto)")
    public CommonResult<ScopeDto> add(@RequestBody @Valid ScopeDto scopeDto, BindingResult result) {
        return scopeService.add(scopeDto);
    }

//    @PostMapping("/page")
//    @RequestLog(method = "ScopeController.page()")
//    public TableData<ScopeDto> page() {
//
//    }
}
