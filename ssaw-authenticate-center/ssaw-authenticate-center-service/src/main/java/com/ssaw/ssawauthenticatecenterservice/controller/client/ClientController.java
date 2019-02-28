package com.ssaw.ssawauthenticatecenterservice.controller.client;

import com.ssaw.commons.annotations.RequestLog;
import com.ssaw.commons.annotations.Validating;
import com.ssaw.commons.vo.CommonResult;
import com.ssaw.commons.vo.PageReqDto;
import com.ssaw.commons.vo.TableData;
import com.ssaw.ssawauthenticatecenterfeign.annotations.Menu;
import com.ssaw.ssawauthenticatecenterfeign.annotations.SecurityApi;
import com.ssaw.ssawauthenticatecenterfeign.annotations.SecurityMethod;
import com.ssaw.ssawauthenticatecenterfeign.vo.ClientDetailsInfoDto;
import com.ssaw.ssawauthenticatecenterfeign.vo.ClientDto;
import com.ssaw.ssawauthenticatecenterservice.controller.BaseController;
import com.ssaw.ssawauthenticatecenterservice.service.ClientService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author HuSen.
 * @date 2018/12/11 11:56.
 */
@Slf4j
@RestController
@RequestMapping("/api/client")
@SecurityApi(index = "1", group = "授权管理", menu = @Menu(index = "1-3", title = "客户端", scope = "CLIENT_MANAGE", to = "/authenticate/center/client"))
public class ClientController extends BaseController {
    private final ClientService clientService;

    @Autowired
    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    /**
     * 根据clientId查询客户端
     * @param clientId clientId
     * @return 客户端
     */
    @GetMapping("/get/{clientId}")
    @RequestLog(desc = "根据clientId查询客户端")
    @SecurityMethod(antMatcher = "/api/client/get/*", scope = "CLIENT_READ")
    public CommonResult<ClientDetailsInfoDto> findById(@PathVariable(name = "clientId") String clientId) {
        return clientService.findById(clientId);
    }

    /**
     * 新增客户端
     * @param clientDto 新增客户端请求对象
     * @return 新增结果
     */
    @Validating
    @PostMapping("/add")
    @RequestLog(desc = "新增客户端")
    @SecurityMethod(antMatcher = "/api/client/add", scope = "CLIENT_CREATE", button = "CLIENT_CREATE", buttonName = "添加")
    public CommonResult<ClientDto> add(@RequestBody ClientDto clientDto) {
        return clientService.save(clientDto);
    }

    /**
     * 分页查询客户端
     * @param pageReq 分页请求参数
     * @return 查询结果
     */
    @PostMapping("/page")
    @RequestLog(desc = "分页查询客户端")
    @SecurityMethod(antMatcher = "/api/client/page", scope = "CLIENT_READ", button = "CLIENT_READ", buttonName = "搜索")
    public TableData<ClientDto> page(PageReqDto<ClientDto> pageReq) {
        return clientService.page(pageReq);
    }

    /**
     * 根据Id删除客户端
     * @param id ID
     * @return 删除结果
     */
    @PostMapping("/delete/{id}")
    @RequestLog(desc = "根据Id删除客户端")
    @SecurityMethod(antMatcher = "/api/client/delete/*", scope = "CLIENT_DELETE", button = "CLIENT_DELETE", buttonName = "删除")
    public CommonResult<String> delete(@PathVariable(name = "id") String id) {
        return clientService.delete(id);
    }
}
