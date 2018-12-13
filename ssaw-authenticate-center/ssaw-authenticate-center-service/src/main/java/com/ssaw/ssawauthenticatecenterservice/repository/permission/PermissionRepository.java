package com.ssaw.ssawauthenticatecenterservice.repository.permission;

import com.ssaw.ssawauthenticatecenterservice.entity.PermissionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * @author HuSen.
 * @date 2018/12/13 17:06.
 */
@Repository
public interface PermissionRepository extends JpaRepository<PermissionEntity, Long>, JpaSpecificationExecutor<PermissionEntity> {

    long countByName(String name);
}
