package com.ssaw.ssawauthenticatecenterservice.service.impl;

import com.ssaw.commons.vo.CommonResult;
import com.ssaw.commons.vo.PageReqDto;
import com.ssaw.commons.vo.TableData;
import com.ssaw.ssawauthenticatecenterfeign.vo.EditClientScopeDto;
import com.ssaw.ssawauthenticatecenterfeign.vo.ResourceDto;
import com.ssaw.ssawauthenticatecenterfeign.vo.TreeDto;
import com.ssaw.ssawauthenticatecenterservice.dao.entity.resource.ResourceEntity;
import com.ssaw.ssawauthenticatecenterservice.dao.entity.scope.ScopeEntity;
import com.ssaw.ssawauthenticatecenterservice.dao.repository.resource.ResourceRepository;
import com.ssaw.ssawauthenticatecenterservice.dao.repository.scope.ScopeRepository;
import com.ssaw.ssawauthenticatecenterservice.service.ResourceService;
import com.ssaw.ssawauthenticatecenterservice.specification.ResourceSpecification;
import com.ssaw.ssawauthenticatecenterservice.transfer.ResourceTransfer;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
    private final ScopeRepository scopeRepository;

    @Autowired
    public ResourceServiceImpl(ResourceTransfer resourceTransfer, ResourceRepository resourceRepository, ScopeRepository scopeRepository) {
        this.resourceTransfer = resourceTransfer;
        this.resourceRepository = resourceRepository;
        this.scopeRepository = scopeRepository;
    }

    /**
     * 新增资源服务
     * @param resourceDto 新增资源服务请求对象
     * @return 新增结果
     */
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

    /**
     * 分页查询资源服务
     * @param pageReqDto 分页查询参数
     * @return 分页结果
     */
    @Override
    public TableData<ResourceDto> page(PageReqDto<ResourceDto> pageReqDto) {
        Pageable pageable = getPageRequest(pageReqDto);
        Page<ResourceEntity> page = resourceRepository.findAll(new ResourceSpecification(pageReqDto.getData()), pageable);
        TableData<ResourceDto> tableData = new TableData<>();
        setTableData(page, tableData);
        tableData.setContent(page.getContent().stream().map(resourceTransfer::entity2Dto).collect(Collectors.toList()));
        return tableData;
    }

    /**
     * 根据ID查询资源服务
     * @param id ID
     * @return 资源服务
     */
    @Override
    public CommonResult<ResourceDto> findById(Long id) {
        if(Objects.isNull(id)) {
            return CommonResult.createResult(PARAM_ERROR, "资源ID为空!", null);
        }
        return resourceRepository.findById(id)
                .map(resourceEntity -> CommonResult.createResult(SUCCESS, "成功!", resourceTransfer.entity2Dto(resourceEntity)))
                .orElseGet(() -> CommonResult.createResult(DATA_NOT_EXIST, "该资源不存在!", null));
    }

    /**
     * 修改资源服务
     * @param resourceDto 修改资源请求对象
     * @return 修改结果
     */
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public CommonResult<ResourceDto> update(ResourceDto resourceDto) {
        return resourceRepository.findById(resourceDto.getId()).map(entity -> {
            if(!StringUtils.equals(entity.getResourceId(), resourceDto.getResourceId())
                    && resourceRepository.countByResourceId(resourceDto.getResourceId()) > 0) {
                return CommonResult.createResult(PARAM_ERROR, "该资源ID已存在!", resourceDto);
            }
            entity.setResourceId(resourceDto.getResourceId());
            entity.setDescription(resourceDto.getDescription());
            entity.setModifyTime(LocalDateTime.now());
            resourceRepository.save(entity);
            return CommonResult.createResult(SUCCESS, "成功!", resourceDto);
        }).orElseGet(() -> CommonResult.createResult(DATA_NOT_EXIST, "该资源不存在!", resourceDto));
    }

    /**
     * 根据ID删除资源服务
     * @param id ID
     * @return 删除结果
     */
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public CommonResult<Long> delete(Long id) {
        if(Objects.isNull(id)) {
            return CommonResult.createResult(PARAM_ERROR, "资源ID为空!", null);
        }
        resourceRepository.deleteById(id);
        return CommonResult.createResult(SUCCESS, "成功!", id);
    }

    /**
     * 根据资源ID查询资源
     * @param resourceId 资源ID
     * @return 资源服务
     */
    @Override
    public CommonResult<List<ResourceDto>> search(String resourceId) {
        Pageable pageable = PageRequest.of(0, 20);
        ResourceDto resourceDto = new ResourceDto();
        resourceDto.setResourceId(StringUtils.equals("none", resourceId) ? "" : resourceId);
        Page<ResourceEntity> page = resourceRepository.findAll(new ResourceSpecification(resourceDto), pageable);
        if(CollectionUtils.isNotEmpty(page.getContent())) {
            List<ResourceDto> resourceDtoList = page.getContent().stream().map(resourceTransfer::entity2Dto).collect(Collectors.toList());
            return CommonResult.createResult(SUCCESS, "成功!", resourceDtoList);
        } else {
            return CommonResult.createResult(SUCCESS, "成功!", new ArrayList<>(0));
        }
    }

    /**
     * 查询所有的资源服务
     * @return 所有的资源服务
     */
    @Override
    public CommonResult<List<ResourceDto>> findAll() {
        return CommonResult.createResult(SUCCESS, "成功!",
                resourceRepository.findAll().stream().map(resourceTransfer::entity2Dto).collect(Collectors.toList()));
    }

    /**
     * 根据资源ID查询出树结构作用域
     * @param ids 资源IDS
     * @return 树结构作用域
     */
    @Override
    public CommonResult<EditClientScopeDto> findAllScopeByResourceIds(String ids) {
        EditClientScopeDto clientScopeDto = new EditClientScopeDto();
        if(StringUtils.isNotEmpty(ids)) {
            List<ResourceEntity> allById = resourceRepository.findAllById(Arrays.stream(ids.split(",")).map(Long::valueOf).collect(Collectors.toList()));
            List<TreeDto> treeDtoList = new ArrayList<>(allById.size());
            for(ResourceEntity entity : allById) {
                TreeDto treeDto = new TreeDto();
                treeDto.setId(entity.getId());
                treeDto.setLabel(entity.getResourceId());
                List<ScopeEntity> allByResourceId = scopeRepository.findAllByResourceId(entity.getId());
                List<TreeDto> children = allByResourceId.stream().map(x -> {
                    TreeDto child = new TreeDto();
                    child.setId(x.getId());
                    child.setLabel(x.getScope());
                    return child;
                }).collect(Collectors.toList());
                treeDto.setChildren(children);
                treeDtoList.add(treeDto);
            }
            clientScopeDto.setTreeDtos(treeDtoList);
        } else {
            clientScopeDto.setTreeDtos(new ArrayList<>(0));
            clientScopeDto.setDefaultExpandedKeys(new ArrayList<>(0));
        }
        return CommonResult.createResult(SUCCESS, "成功!", clientScopeDto);
    }

    /**
     * 上传资源服务
     * @param resourceDto 上传资源服务请求对象
     * @return 上传结果
     */
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public CommonResult<ResourceDto> uploadResource(ResourceDto resourceDto) {
        ResourceEntity byResourceId = resourceRepository.findByResourceId(resourceDto.getResourceId());
        ResourceEntity resourceEntity;
        if (Objects.isNull(byResourceId)) {
            ResourceEntity entity = resourceTransfer.dto2Entity(resourceDto);
            entity.setCreateTime(LocalDateTime.now());
            resourceEntity = resourceRepository.save(entity);
        } else {
            byResourceId.setResourceId(resourceDto.getResourceId());
            byResourceId.setDescription(resourceDto.getDescription());
            byResourceId.setModifyTime(LocalDateTime.now());
            resourceEntity = resourceRepository.save(byResourceId);
        }
        resourceDto.setId(resourceEntity.getId());
        return CommonResult.createResult(SUCCESS, "成功", resourceDto);
    }
}
