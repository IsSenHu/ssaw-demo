package com.ssaw.ssawauthenticatecenterservice.controller;

import com.ssaw.commons.annotations.RequestLog;
import com.ssaw.commons.annotations.Validating;
import com.ssaw.commons.vo.CommonResult;
import com.ssaw.commons.vo.PageReqDto;
import com.ssaw.commons.vo.TableData;
import com.ssaw.ssawauthenticatecenterfeign.annotations.Menu;
import com.ssaw.ssawauthenticatecenterfeign.annotations.SecurityApi;
import com.ssaw.ssawauthenticatecenterfeign.annotations.SecurityMethod;
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
@SecurityApi(index = "1", group = "授权管理", menu = @Menu(index = "1-2", title = "作用域", scope = "SCOPE_MANAGE", to = "/authenticate/center/scope"))
public class ScopeController extends BaseController {

    private final ScopeService scopeService;

    @Autowired
    public ScopeController(ScopeService scopeService) {
        this.scopeService = scopeService;
    }

    @Validating
    @PostMapping("/add")
    @RequestLog(method = "ScopeController.add(ScopeDto scopeDto)")
    @SecurityMethod(antMatcher = "/api/scope/add", scope = "SCOPE_CREATE", button = "SCOPE_CREATE", buttonName = "添加")
    public CommonResult<ScopeDto> add(@RequestBody @Valid ScopeDto scopeDto, BindingResult result) {
        return scopeService.add(scopeDto);
    }

    @PostMapping("/uploadScopes")
    public CommonResult<String> uploadScopes(@RequestBody List<ScopeDto> scopeDtoList) {
        // TODO 安全策略
        return scopeService.uploadScopes(scopeDtoList);
    }

    @PostMapping("/page")
    @RequestLog(method = "ScopeController.page(PageReqDto<ScopeDto> pageReqDto)")
    @SecurityMethod(antMatcher = "/api/scope/page", scope = "SCOPE_READ", button = "SCOPE_READ", buttonName = "搜索")
    public TableData<ScopeDto> page(@RequestBody PageReqDto<ScopeDto> pageReqDto) {
        return scopeService.page(pageReqDto);
    }

    @PostMapping("/delete/{id}")
    @RequestLog(method = "ScopeController.delete(Long id)")
    @SecurityMethod(antMatcher = "/api/scope/delete/*", scope = "SCOPE_DELETE", button = "SCOPE_DELETE", buttonName = "删除")
    public CommonResult<Long> delete(@PathVariable(name = "id") Long id) {
        return scopeService.delete(id);
    }

    @GetMapping("/findById/{id}")
    @RequestLog(method = "ScopeController.findById(Long id)")
    @SecurityMethod(antMatcher = "/api/scope/findById/*", scope = "SCOPE_READ")
    public CommonResult<ScopeDto> findById(@PathVariable(name = "id") Long id) {
        return scopeService.findById(id);
    }

    @Validating
    @PostMapping("/update")
    @RequestLog(method = "ScopeController.update(ScopeDto scopeDto)")
    @SecurityMethod(antMatcher = "/api/scope/update", scope = "SCOPE_UPDATE", button = "SCOPE_UPDATE", buttonName = "编辑")
    public CommonResult<ScopeDto> update(@RequestBody @Valid ScopeDto scopeDto, BindingResult result) {
        return scopeService.update(scopeDto);
    }

    @GetMapping("/search/{scope}")
    @RequestLog(method = "ScopeController.search(Long scopeId)")
    @SecurityMethod(antMatcher = "/api/scope/search/*", scope = "SCOPE_READ")
    public CommonResult<List<ScopeDto>> search(@PathVariable(name = "scope") String scope) {
        return scopeService.search(scope);
    }

    @GetMapping("/searchForUpdate/{scope}")
    @RequestLog(method = "ScopeController.searchForUpdate(Long scopeId)")
    @SecurityMethod(antMatcher = "/api/scope/searchForUpdate/*", scope = "SCOPE_READ")
    public CommonResult<List<ScopeDto>> searchForUpdate(@PathVariable(name = "scope") String scope) {
        return scopeService.searchForUpdate(scope);
    }
}
