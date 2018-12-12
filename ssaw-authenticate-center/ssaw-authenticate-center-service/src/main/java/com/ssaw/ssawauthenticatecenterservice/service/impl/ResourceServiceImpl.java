package com.ssaw.ssawauthenticatecenterservice.service.impl;

import com.ssaw.commons.vo.CommonResult;
import com.ssaw.commons.vo.PageReqDto;
import com.ssaw.commons.vo.TableData;
import com.ssaw.ssawauthenticatecenterfeign.dto.ResourceDto;
import com.ssaw.ssawauthenticatecenterservice.entity.ResourceEntity;
import com.ssaw.ssawauthenticatecenterservice.repository.resource.ResourceRepository;
import com.ssaw.ssawauthenticatecenterservice.service.ResourceService;
import com.ssaw.ssawauthenticatecenterservice.specification.ResourceSpecification;
import com.ssaw.ssawauthenticatecenterservice.transfer.ResourceTransfer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.stream.Collectors;
import static com.ssaw.commons.constant.Constants.ResultCodes.*;

/**
 * @author HuSen.
 * @date 2018/12/12 13:59.
 */
@Slf4j
@Service
public class ResourceServiceImpl extends BaseService implements ResourceService {

    private final ResourceTransfer resourceTransfer;
    private final ResourceRepository resourceRepository;

    @Autowired
    public ResourceServiceImpl(ResourceTransfer resourceTransfer, ResourceRepository resourceRepository) {
        this.resourceTransfer = resourceTransfer;
        this.resourceRepository = resourceRepository;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public CommonResult<ResourceDto> add(ResourceDto resourceDto) {
        ResourceEntity entity = resourceTransfer.dto2Entity(resourceDto);
        if(Objects.isNull(entity)) {
            return CommonResult.createResult(PARAM_ERROR, "参数为空!", null);
        }
        if(resourceRepository.countByResourceId(entity.getResourceId()) > 0) {
            return CommonResult.createResult(DATA_EXIST, "该资源ID已存在!", resourceDto);
        }
        entity.setCreateTime(LocalDateTime.now());
        resourceRepository.save(entity);
        return CommonResult.createResult(SUCCESS, "成功!", resourceDto);
    }

    @Override
    public TableData<ResourceDto> page(PageReqDto<ResourceDto> pageReqDto) {
        PageRequest pageRequest = getPageRequest(pageReqDto);
        Page<ResourceEntity> page = resourceRepository.findAll(new ResourceSpecification(pageReqDto.getData()), pageRequest);
        TableData<ResourceDto> tableData = new TableData<>();
        tableData.setPage(page.getNumber() + 1);
        tableData.setSize(page.getSize());
        tableData.setTotalPages(page.getTotalPages());
        tableData.setTotals(page.getTotalElements());
        tableData.setContent(page.getContent().stream().map(resourceTransfer::entity2Dto).collect(Collectors.toList()));
        return tableData;
    }

    @Override
    public CommonResult<ResourceDto> findById(Long id) {
        if(Objects.isNull(id)) {
            return CommonResult.createResult(PARAM_ERROR, "资源ID为空!", null);
        }
        return resourceRepository.findById(id)
                .map(resourceEntity -> CommonResult.createResult(SUCCESS, "成功!", resourceTransfer.entity2Dto(resourceEntity)))
                .orElseGet(() -> CommonResult.createResult(DATA_NOT_EXIST, "该资源不存在!", null));
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public CommonResult<ResourceDto> update(ResourceDto resourceDto) {
        return null;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public CommonResult<Long> delete(Long id) {
        if(Objects.isNull(id)) {
            return CommonResult.createResult(PARAM_ERROR, "资源ID为空!", null);
        }
        resourceRepository.deleteById(id);
        return CommonResult.createResult(SUCCESS, "成功!", id);
    }
}
