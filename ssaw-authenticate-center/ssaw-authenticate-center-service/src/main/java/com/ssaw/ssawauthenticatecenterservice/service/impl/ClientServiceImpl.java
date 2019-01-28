package com.ssaw.ssawauthenticatecenterservice.service.impl;

import com.ssaw.commons.security.SecurityUtils;
import com.ssaw.commons.vo.CommonResult;
import com.ssaw.commons.vo.PageReqDto;
import com.ssaw.commons.vo.TableData;
import com.ssaw.ssawauthenticatecenterfeign.dto.ClientDetailsInfoDto;
import com.ssaw.ssawauthenticatecenterfeign.dto.ClientDto;
import com.ssaw.ssawauthenticatecenterservice.entity.ClientDetailsEntity;
import com.ssaw.ssawauthenticatecenterservice.entity.ResourceEntity;
import com.ssaw.ssawauthenticatecenterservice.repository.client.ClientRepository;
import com.ssaw.ssawauthenticatecenterservice.repository.resource.ResourceRepository;
import com.ssaw.ssawauthenticatecenterservice.service.ClientService;
import com.ssaw.ssawauthenticatecenterservice.specification.ClientSpecification;
import com.ssaw.ssawauthenticatecenterservice.transfer.ClientTransfer;
import com.ssaw.ssawauthenticatecenterservice.vo.UserVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.exceptions.InvalidClientException;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.ssaw.commons.constant.Constants.ResultCodes.*;
import static com.ssaw.ssawauthenticatecenterservice.constants.ClientConstant.AuthorizedGrantTypes.*;

/**
 * @author HuSen.
 * @date 2018/12/11 9:33.
 */
@Slf4j
@Service
public class ClientServiceImpl extends BaseService implements ClientService {

    private final ClientRepository clientRepository;
    private final ClientTransfer clientTransfer;
    private final ResourceRepository resourceRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public ClientServiceImpl(ClientRepository clientRepository, ClientTransfer clientTransfer, PasswordEncoder passwordEncoder, ResourceRepository resourceRepository) {
        this.clientRepository = clientRepository;
        this.clientTransfer = clientTransfer;
        this.passwordEncoder = passwordEncoder;
        this.resourceRepository = resourceRepository;
    }

    @Override
    public ClientDetails loadClientByClientId(String clientId) {
        Optional<ClientDetailsEntity> optionalDetailsEntity = clientRepository.findById(clientId);
        if(!optionalDetailsEntity.isPresent()) {
            throw new InvalidClientException("This client(" + clientId + ") is invalid");
        }
        // 校验client与用户的绑定关系
        UserVo userDetails = SecurityUtils.getUserDetails(UserVo.class);
        ClientDetailsEntity clientDetailsEntity = optionalDetailsEntity.get();
        if(!Objects.isNull(userDetails) && !userDetails.getId().equals(clientDetailsEntity.getUserId())) {
            throw new InvalidClientException("This user(" + userDetails.getId() + ") is not match this client(" + clientId + ")");
        }
        return clientDetailsEntity;
    }

    @Override
    public CommonResult<ClientDetailsInfoDto> findById(String clientId) {
        return clientRepository.findById(clientId)
                .map(entity -> CommonResult.createResult(SUCCESS, "成功!", clientTransfer.entity2DetailsInfoDto(entity)))
                .orElseGet(() -> CommonResult.createResult(DATA_NOT_EXIST, "客户端不存在!", null));
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public CommonResult<ClientDto> save(ClientDto clientDto) {
        if(null != clientDto) {

            // 校验ClientId是否唯一
            if(clientRepository.countByClientId(clientDto.getClientId()) > 0) {
                return CommonResult.createResult(DATA_EXIST, "该ClientId已有!", clientDto);
            }

            // 检查授权方式与是否刷新token是否设置合法
            String authorizedGrantTypes = clientDto.getAuthorizedGrantTypes();
            if(authorizedGrantTypes.contains(REFRESH_TOKEN.getValue())) {
                if(!authorizedGrantTypes.contains(AUTHORIZATION_CODE.getValue()) && !authorizedGrantTypes.contains(PASSWORD.getValue())) {
                    return CommonResult.createResult(PARAM_ERROR, "授权方式设置错误，设置的授权类型不支持refresh_token", clientDto);
                }
            }

            // 将资源的id转为resourceId
            List<ResourceEntity> allById = resourceRepository.findAllById(Arrays.stream(clientDto.getResourceIds().split(",")).map(Long::valueOf).collect(Collectors.toList()));
            String resourceIds = allById.stream().map(ResourceEntity::getResourceId).collect(Collectors.joining(","));
            clientDto.setResourceIds(resourceIds);

            // 加密密码
            clientDto.setClientSecret(passwordEncoder.encode(clientDto.getClientSecret()));
            // 创建时间
            clientDto.setCreateTime(LocalDateTime.now());
            // token过期时间
            clientDto.setAccessTokenValiditySeconds(null == clientDto.getAccessTokenValiditySeconds() ? 60 * 60 * 24 * 30 : clientDto.getAccessTokenValiditySeconds());
            // refresh token过期时间
            clientDto.setRefreshTokenValiditySeconds(null == clientDto.getRefreshTokenValiditySeconds() ? 60 * 60 * 24 * 30 : clientDto.getRefreshTokenValiditySeconds());

            ClientDetailsEntity entity = clientTransfer.dto2Entity(clientDto);
            clientRepository.save(entity);
            return CommonResult.createResult(SUCCESS, "成功!", clientDto);
        }
        return CommonResult.createResult(PARAM_ERROR, "参数错误!", null);
    }

    @Override
    public TableData<ClientDto> page(PageReqDto<ClientDto> pageReq) {
        Pageable pageable = getPageRequest(pageReq);
        Page<ClientDetailsEntity> page = clientRepository.findAll(new ClientSpecification(pageReq.getData()), pageable);
        TableData<ClientDto> tableData = new TableData<>();
        tableData.setContent(page.getContent().stream().map(clientTransfer::entity2Dto).collect(Collectors.toList()));
        setTableData(page, tableData);
        return tableData;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public CommonResult<String> delete(String id) {
        clientRepository.deleteById(id);
        return CommonResult.createResult(SUCCESS, "成功!", id);
    }
}
