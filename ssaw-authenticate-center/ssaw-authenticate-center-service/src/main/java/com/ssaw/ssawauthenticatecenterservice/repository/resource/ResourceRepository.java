package com.ssaw.ssawauthenticatecenterservice.repository.resource;

import com.ssaw.ssawauthenticatecenterservice.entity.ResourceEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * @author HuSen.
 * @date 2018/12/12 10:34.
 */
@Repository
public interface ResourceRepository extends JpaRepository<ResourceEntity, Long>, JpaSpecificationExecutor<ResourceEntity> {
    long countByResourceId(String resourceId);
}
