package com.ssaw.ssawauthenticatecenterservice.controller;

import com.ssaw.commons.annotations.RequestLog;
import com.ssaw.commons.annotations.Validating;
import com.ssaw.commons.vo.CommonResult;
import com.ssaw.commons.vo.PageReqDto;
import com.ssaw.commons.vo.TableData;
import com.ssaw.ssawauthenticatecenterfeign.dto.ScopeDto;
import com.ssaw.ssawauthenticatecenterservice.service.ScopeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

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

    @PostMapping("/page")
    @RequestLog(method = "ScopeController.page(PageReqDto<ScopeDto> pageReqDto)")
    public TableData<ScopeDto> page(@RequestBody PageReqDto<ScopeDto> pageReqDto) {
        return scopeService.page(pageReqDto);
    }

    @PostMapping("/delete/{id}")
    @RequestLog(method = "ScopeController.delete(Long id)")
    public CommonResult<Long> delete(@PathVariable(name = "id") Long id) {
        return scopeService.delete(id);
    }

    @GetMapping("/findById/{id}")
    @RequestLog(method = "ScopeController.findById(Long id)")
    public CommonResult<ScopeDto> findById(@PathVariable(name = "id") Long id) {
        return scopeService.findById(id);
    }

    @Validating
    @PostMapping("/update")
    @RequestLog(method = "ScopeController.update(ScopeDto scopeDto)")
    public CommonResult<ScopeDto> update(@RequestBody @Valid ScopeDto scopeDto, BindingResult result) {
        return scopeService.update(scopeDto);
    }

    @GetMapping("/search/{scope}")
    @RequestLog(method = "ScopeController.search(Long scopeId)")
    public CommonResult<List<ScopeDto>> search(@PathVariable(name = "scope") String scope) {
        return scopeService.search(scope);
    }
}
