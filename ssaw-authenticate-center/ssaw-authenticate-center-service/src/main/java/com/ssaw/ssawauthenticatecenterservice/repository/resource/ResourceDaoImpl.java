package com.ssaw.ssawauthenticatecenterservice.repository.resource;

import com.ssaw.ssawauthenticatecenterservice.entity.ResourceEntity;
import com.ssaw.ssawauthenticatecenterservice.repository.ResourceDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import javax.persistence.EntityManager;

/**
 * @author HuSen.
 * @date 2018/12/12 11:25.
 */
public class ResourceDaoImpl extends SimpleJpaRepository<ResourceEntity, Long> implements ResourceDao {

    public ResourceDaoImpl(@Autowired EntityManager entityManager) {
        super(ResourceEntity.class, entityManager);
        em = entityManager;
    }
    private final EntityManager em;
}
