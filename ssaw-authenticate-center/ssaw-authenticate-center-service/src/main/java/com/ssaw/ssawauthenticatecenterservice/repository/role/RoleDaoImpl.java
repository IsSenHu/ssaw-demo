package com.ssaw.ssawauthenticatecenterservice.repository.role;

import com.ssaw.ssawauthenticatecenterservice.entity.RoleEntity;
import com.ssaw.ssawauthenticatecenterservice.repository.RoleDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import javax.persistence.EntityManager;

/**
 * @author HuSen.
 * @date 2018/12/14 17:51.
 */
public class RoleDaoImpl extends SimpleJpaRepository<RoleEntity, Long> implements RoleDao {
    public RoleDaoImpl(@Autowired EntityManager entityManager) {
        super(RoleEntity.class, entityManager);
        em = entityManager;
    }

    private final EntityManager em;
}
