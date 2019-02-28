package com.ssaw.ssawauthenticatecenterservice.service;

import com.ssaw.commons.vo.CommonResult;
import com.ssaw.commons.vo.PageReqDto;
import com.ssaw.commons.vo.TableData;
import com.ssaw.ssawauthenticatecenterfeign.vo.ClientDetailsInfoDto;
import com.ssaw.ssawauthenticatecenterfeign.vo.ClientDto;
import org.springframework.security.oauth2.provider.ClientDetailsService;

/**
 * @author HuSen.
 * @date 2018/12/11 9:32.
 */
public interface ClientService extends ClientDetailsService {

    /**
     * 根据clientId查询客户端
     * @param clientId clientId
     * @return 客户端
     */
    CommonResult<ClientDetailsInfoDto> findById(String clientId);

    /**
     * 新增客户端
     * @param clientDto 新增客户端请求对象
     * @return 新增结果
     */
    CommonResult<ClientDto> save(ClientDto clientDto);

    /**
     * 分页查询客户端
     * @param pageReq 分页请求参数
     * @return 查询结果
     */
    TableData<ClientDto> page(PageReqDto<ClientDto> pageReq);

    /**
     * 根据Id删除客户端
     * @param id ID
     * @return 删除结果
     */
    CommonResult<String> delete(String id);
}
