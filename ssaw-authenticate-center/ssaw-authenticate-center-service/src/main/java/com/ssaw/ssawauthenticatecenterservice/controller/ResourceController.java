package com.ssaw.ssawauthenticatecenterservice.controller;

import com.ssaw.commons.annotations.RequestLog;
import com.ssaw.commons.annotations.Validating;
import com.ssaw.commons.vo.CommonResult;
import com.ssaw.commons.vo.PageReqDto;
import com.ssaw.commons.vo.TableData;
import com.ssaw.ssawauthenticatecenterfeign.annotations.Menu;
import com.ssaw.ssawauthenticatecenterfeign.annotations.SecurityApi;
import com.ssaw.ssawauthenticatecenterfeign.annotations.SecurityMethod;
import com.ssaw.ssawauthenticatecenterfeign.dto.EditClientScopeDto;
import com.ssaw.ssawauthenticatecenterfeign.dto.ResourceDto;
import com.ssaw.ssawauthenticatecenterservice.service.ResourceService;
import com.ssaw.ssawauthenticatecenterservice.service.impl.ResourceServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;

/**
 * @author HuSen.
 * @date 2018/12/12 13:58.
 */
@RestController
@RequestMapping("/api/resource")
@SecurityApi(index = "1", group = "授权管理", menu = @Menu(index = "1-1", title = "资源服务", scope = "RESOURCE_MANAGE", to = "/authenticate/center/resource"))
public class ResourceController extends BaseController {

    private final ResourceService resourceService;

    @Autowired
    public ResourceController(ResourceServiceImpl resourceService) {
        this.resourceService = resourceService;
    }

    @Validating
    @PostMapping("/add")
    @RequestLog(method = "ResourceController.add(ResourceDto resourceDto)")
    @SecurityMethod(antMatcher = "/api/resource/add", scope = "RESOURCE_CREATE", button = "RESOURCE_CREATE", buttonName = "添加")
    public CommonResult<ResourceDto> add(@RequestBody @Valid ResourceDto resourceDto, BindingResult result) {
        return resourceService.add(resourceDto);
    }

    @PostMapping("/uploadResource")
    public CommonResult<ResourceDto> uploadResource(@RequestBody ResourceDto resourceDto) {
        // TODO 这里的安全策略
        return resourceService.uploadResource(resourceDto);
    }

    @PostMapping("/page")
    @RequestLog(method = "ResourceController.page(PageReqDto<ResourceDto> pageReqDto)")
    @SecurityMethod(antMatcher = "/api/resource/page", scope = "RESOURCE_READ", button = "RESOURCE_READ", buttonName = "搜索")
    public TableData<ResourceDto> page(@RequestBody PageReqDto<ResourceDto> pageReqDto) {
        return resourceService.page(pageReqDto);
    }

    @GetMapping("/findById/{id}")
    @RequestLog(method = "ResourceController.findById(Long id)")
    @SecurityMethod(antMatcher = "/api/resource/findById/*", scope = "RESOURCE_READ")
    public CommonResult<ResourceDto> findById(@PathVariable(name = "id") Long id) {
        return resourceService.findById(id);
    }

    @Validating
    @PostMapping("/update")
    @RequestLog(method = "ResourceController.update(ResourceDto resourceDto)")
    @SecurityMethod(antMatcher = "/api/resource/update", scope = "RESOURCE_UPDATE", button = "RESOURCE_UPDATE", buttonName = "编辑")
    public CommonResult<ResourceDto> update(@RequestBody @Valid ResourceDto resourceDto, BindingResult result) {
        return resourceService.update(resourceDto);
    }

    @PostMapping("/delete/{id}")
    @RequestLog(method = "ResourceController.delete(Long id)")
    @SecurityMethod(antMatcher = "/api/resource/delete/*", scope = "RESOURCE_DELETE", button = "RESOURCE_DELETE", buttonName = "删除")
    public CommonResult<Long> delete(@PathVariable(name = "id") Long id) {
        return resourceService.delete(id);
    }

    @GetMapping("/search/{resourceId}")
    @RequestLog(method = "ResourceController.search(String resourceId)")
    @SecurityMethod(antMatcher = "/api/resource/search/*", scope = "RESOURCE_READ")
    public CommonResult<List<ResourceDto>> search(@PathVariable(name = "resourceId") String resourceId) {
        return resourceService.search(resourceId);
    }

    @GetMapping("/findAll")
    @RequestLog(method = "ResourceController.findAll()")
    @SecurityMethod(antMatcher = "/api/resource/findAll", scope = "RESOURCE_READ")
    public CommonResult<List<ResourceDto>> findAll() {
        return resourceService.findAll();
    }

    @GetMapping("/findAllScopeByResourceIds")
    @RequestLog(method = "ResourceController.findAllScopeByResourceIds(String ids)")
    @SecurityMethod(antMatcher = "/api/resource/findAllScopeByResourceIds", scope = "RESOURCE_READ")
    public CommonResult<EditClientScopeDto> findAllScopeByResourceIds(String ids) {
        return resourceService.findAllScopeByResourceIds(ids);
    }
}
