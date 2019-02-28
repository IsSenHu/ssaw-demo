package com.ssaw.ssawauthenticatecenterservice.controller.scope;

import com.ssaw.commons.annotations.RequestLog;
import com.ssaw.commons.annotations.Validating;
import com.ssaw.commons.vo.CommonResult;
import com.ssaw.commons.vo.PageReqDto;
import com.ssaw.commons.vo.TableData;
import com.ssaw.ssawauthenticatecenterfeign.annotations.Menu;
import com.ssaw.ssawauthenticatecenterfeign.annotations.SecurityApi;
import com.ssaw.ssawauthenticatecenterfeign.annotations.SecurityMethod;
import com.ssaw.ssawauthenticatecenterfeign.vo.ScopeDto;
import com.ssaw.ssawauthenticatecenterservice.controller.BaseController;
import com.ssaw.ssawauthenticatecenterservice.service.ScopeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    /**
     * 新增作用域
     * @param scopeDto 新增作用域请求对象
     * @return 新增结果
     */
    @Validating
    @PostMapping("/add")
    @RequestLog(desc = "新增作用域")
    @SecurityMethod(antMatcher = "/api/scope/add", scope = "SCOPE_CREATE", button = "SCOPE_CREATE", buttonName = "添加")
    public CommonResult<ScopeDto> add(@RequestBody ScopeDto scopeDto) {
        return scopeService.add(scopeDto);
    }

    /**
     * 上传作用域
     * @param scopeDtoList 作用域集合
     * @return 上传结果
     */
    @PostMapping("/uploadScopes")
    public CommonResult<String> uploadScopes(@RequestBody List<ScopeDto> scopeDtoList) {
        // TODO 安全策略
        return scopeService.uploadScopes(scopeDtoList);
    }

    /**
     * 分页查询作用域
     * @param pageReqDto 分页查询参数
     * @return 分页结果
     */
    @PostMapping("/page")
    @RequestLog(desc = "分页查询作用域")
    @SecurityMethod(antMatcher = "/api/scope/page", scope = "SCOPE_READ", button = "SCOPE_READ", buttonName = "搜索")
    public TableData<ScopeDto> page(@RequestBody PageReqDto<ScopeDto> pageReqDto) {
        return scopeService.page(pageReqDto);
    }

    /**
     * 根据ID删除作用域
     * @param id ID
     * @return 删除结果
     */
    @PostMapping("/delete/{id}")
    @RequestLog(desc = "根据ID删除作用域")
    @SecurityMethod(antMatcher = "/api/scope/delete/*", scope = "SCOPE_DELETE", button = "SCOPE_DELETE", buttonName = "删除")
    public CommonResult<Long> delete(@PathVariable(name = "id") Long id) {
        return scopeService.delete(id);
    }

    /**
     * 根据ID查询作用域
     * @param id ID
     * @return 作用域
     */
    @GetMapping("/findById/{id}")
    @RequestLog(desc = "根据ID查询作用域")
    @SecurityMethod(antMatcher = "/api/scope/findById/*", scope = "SCOPE_READ")
    public CommonResult<ScopeDto> findById(@PathVariable(name = "id") Long id) {
        return scopeService.findById(id);
    }

    /**
     * 修改作用域
     * @param scopeDto 修改作用域请求对象
     * @return 修改结果
     */
    @Validating
    @PostMapping("/update")
    @RequestLog(desc = "修改作用域请求对象")
    @SecurityMethod(antMatcher = "/api/scope/update", scope = "SCOPE_UPDATE", button = "SCOPE_UPDATE", buttonName = "编辑")
    public CommonResult<ScopeDto> update(@RequestBody ScopeDto scopeDto) {
        return scopeService.update(scopeDto);
    }

    /**
     * 根据作用域名称搜索作用域
     * @param scope 作用域名称
     * @return 作用域
     */
    @GetMapping("/search/{scope}")
    @RequestLog(desc = "根据作用域名称搜索作用域")
    @SecurityMethod(antMatcher = "/api/scope/search/*", scope = "SCOPE_READ")
    public CommonResult<List<ScopeDto>> search(@PathVariable(name = "scope") String scope) {
        return scopeService.search(scope);
    }

    /**
     * 根据作用域名称搜索作用域
     * @param scope 作用域名称
     * @return 作用域
     */
    @GetMapping("/searchForUpdate/{scope}")
    @RequestLog(desc = "ScopeController.searchForUpdate(Long scopeId)")
    @SecurityMethod(antMatcher = "/api/scope/searchForUpdate/*", scope = "SCOPE_READ")
    public CommonResult<List<ScopeDto>> searchForUpdate(@PathVariable(name = "scope") String scope) {
        return scopeService.searchForUpdate(scope);
    }
}
