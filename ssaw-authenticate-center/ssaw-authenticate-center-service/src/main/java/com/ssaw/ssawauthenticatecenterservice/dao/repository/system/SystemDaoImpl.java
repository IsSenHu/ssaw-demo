package com.ssaw.ssawauthenticatecenterservice.dao.repository.system;

import com.ssaw.ssawauthenticatecenterservice.dao.entity.system.SystemEntity;
import com.ssaw.ssawauthenticatecenterservice.dao.repository.SystemDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import javax.persistence.EntityManager;

/**
 * @author HuSen
 * @date 2019/2/28 17:11
 */
public class SystemDaoImpl extends SimpleJpaRepository<SystemEntity, Long> implements SystemDao {

    public SystemDaoImpl(@Autowired EntityManager entityManager) {
        super(SystemEntity.class, entityManager);
        em = entityManager;
    }

    private final EntityManager em;
}