package com.ssaw.ssawuserresourceservice.controller;

import com.ssaw.commons.annotations.RequestLog;
import com.ssaw.commons.vo.CommonResult;
import com.ssaw.ssawuserresourcefeign.dto.ClientDto;
import com.ssaw.ssawuserresourceservice.service.ClientService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author HuSen.
 * @date 2018/12/11 11:56.
 */
@Slf4j
@RestController
@RequestMapping("/api/client")
public class ClientController extends BaseController {
    private final ClientService clientService;

    @Autowired
    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping("/get/{clientId}")
    @RequestLog(method = "查询客户端>>>[findById(String clientId)]")
    public CommonResult<ClientDto> findById(@PathVariable(name = "clientId") String clientId) {
        return clientService.findById(clientId);
    }
}
