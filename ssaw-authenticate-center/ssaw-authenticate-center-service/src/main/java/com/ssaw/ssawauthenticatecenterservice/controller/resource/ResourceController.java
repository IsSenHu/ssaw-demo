package com.ssaw.ssawauthenticatecenterservice.controller.resource;

import com.ssaw.commons.annotations.RequestLog;
import com.ssaw.commons.annotations.Validating;
import com.ssaw.commons.vo.CommonResult;
import com.ssaw.commons.vo.PageReqDto;
import com.ssaw.commons.vo.TableData;
import com.ssaw.ssawauthenticatecenterfeign.annotations.Menu;
import com.ssaw.ssawauthenticatecenterfeign.annotations.SecurityApi;
import com.ssaw.ssawauthenticatecenterfeign.annotations.SecurityMethod;
import com.ssaw.ssawauthenticatecenterfeign.vo.EditClientScopeDto;
import com.ssaw.ssawauthenticatecenterfeign.vo.ResourceDto;
import com.ssaw.ssawauthenticatecenterservice.controller.BaseController;
import com.ssaw.ssawauthenticatecenterservice.service.ResourceService;
import com.ssaw.ssawauthenticatecenterservice.service.impl.ResourceServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
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

    /**
     * 新增资源服务
     * @param resourceDto 新增资源服务请求对象
     * @return 新增结果
     */
    @Validating
    @PostMapping("/add")
    @RequestLog(desc = "新增资源服务")
    @SecurityMethod(antMatcher = "/api/resource/add", scope = "RESOURCE_CREATE", button = "RESOURCE_CREATE", buttonName = "添加")
    public CommonResult<ResourceDto> add(@RequestBody ResourceDto resourceDto) {
        return resourceService.add(resourceDto);
    }

    /**
     * 上传资源服务
     * @param resourceDto 上传资源服务请求对象
     * @return 上传结果
     */
    @PostMapping("/uploadResource")
    public CommonResult<ResourceDto> uploadResource(@RequestBody ResourceDto resourceDto) {
        // TODO 这里的安全策略
        return resourceService.uploadResource(resourceDto);
    }

    /**
     * 分页查询资源服务
     * @param pageReqDto 分页查询参数
     * @return 分页结果
     */
    @PostMapping("/page")
    @RequestLog(desc = "分页查询资源服务")
    @SecurityMethod(antMatcher = "/api/resource/page", scope = "RESOURCE_READ", button = "RESOURCE_READ", buttonName = "搜索")
    public TableData<ResourceDto> page(@RequestBody PageReqDto<ResourceDto> pageReqDto) {
        return resourceService.page(pageReqDto);
    }

    /**
     * 根据ID查询资源服务
     * @param id ID
     * @return 资源服务
     */
    @GetMapping("/findById/{id}")
    @RequestLog(desc = "根据ID查询资源服务")
    @SecurityMethod(antMatcher = "/api/resource/findById/*", scope = "RESOURCE_READ")
    public CommonResult<ResourceDto> findById(@PathVariable(name = "id") Long id) {
        return resourceService.findById(id);
    }

    /**
     * 修改资源服务
     * @param resourceDto 修改资源请求对象
     * @return 修改结果
     */
    @Validating
    @PostMapping("/update")
    @RequestLog(desc = "修改资源服务")
    @SecurityMethod(antMatcher = "/api/resource/update", scope = "RESOURCE_UPDATE", button = "RESOURCE_UPDATE", buttonName = "编辑")
    public CommonResult<ResourceDto> update(@RequestBody ResourceDto resourceDto) {
        return resourceService.update(resourceDto);
    }

    /**
     * 根据ID删除资源服务
     * @param id ID
     * @return 删除结果
     */
    @PostMapping("/delete/{id}")
    @RequestLog(desc = "根据ID删除资源服务")
    @SecurityMethod(antMatcher = "/api/resource/delete/*", scope = "RESOURCE_DELETE", button = "RESOURCE_DELETE", buttonName = "删除")
    public CommonResult<Long> delete(@PathVariable(name = "id") Long id) {
        return resourceService.delete(id);
    }

    /**
     * 根据资源ID查询资源
     * @param resourceId 资源ID
     * @return 资源服务
     */
    @GetMapping("/search/{resourceId}")
    @RequestLog(desc = "根据资源ID查询资源")
    @SecurityMethod(antMatcher = "/api/resource/search/*", scope = "RESOURCE_READ")
    public CommonResult<List<ResourceDto>> search(@PathVariable(name = "resourceId") String resourceId) {
        return resourceService.search(resourceId);
    }

    /**
     * 查询所有的资源服务
     * @return 所有的资源服务
     */
    @GetMapping("/findAll")
    @RequestLog(desc = "查询所有的资源服务")
    @SecurityMethod(antMatcher = "/api/resource/findAll", scope = "RESOURCE_READ")
    public CommonResult<List<ResourceDto>> findAll() {
        return resourceService.findAll();
    }

    /**
     * 根据资源ID查询出树结构作用域
     * @param ids 资源IDS
     * @return 树结构作用域
     */
    @GetMapping("/findAllScopeByResourceIds")
    @RequestLog(desc = "根据资源ID查询出树结构作用域")
    @SecurityMethod(antMatcher = "/api/resource/findAllScopeByResourceIds", scope = "RESOURCE_READ")
    public CommonResult<EditClientScopeDto> findAllScopeByResourceIds(String ids) {
        return resourceService.findAllScopeByResourceIds(ids);
    }
}
