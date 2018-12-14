package com.ssaw.ssawauthenticatecenterservice.repository.role.permission;

import com.ssaw.ssawauthenticatecenterservice.entity.RolePermissionEntity;
import com.ssaw.ssawauthenticatecenterservice.repository.RolePermissionDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import javax.persistence.EntityManager;

/**
 * @author HuSen.
 * @date 2018/12/14 20:23.
 */
public class RolePermissionDaoImpl extends SimpleJpaRepository<RolePermissionEntity, Long> implements RolePermissionDao {
    public RolePermissionDaoImpl(@Autowired EntityManager entityManager) {
        super(RolePermissionEntity.class, entityManager);
        em = entityManager;
    }

    private final EntityManager em;
}
