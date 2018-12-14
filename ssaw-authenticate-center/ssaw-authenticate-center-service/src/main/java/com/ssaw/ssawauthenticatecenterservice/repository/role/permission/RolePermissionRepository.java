package com.ssaw.ssawauthenticatecenterservice.repository.role.permission;

import com.ssaw.ssawauthenticatecenterservice.entity.RolePermissionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author HuSen.
 * @date 2018/12/14 20:15.
 */
@Repository
public interface RolePermissionRepository extends JpaRepository<RolePermissionEntity, Long>, JpaSpecificationExecutor<RolePermissionEntity> {

    List<RolePermissionEntity> findAllByRoleId(Long roleId);

    void deleteAllByRoleId(Long roleId);
}
