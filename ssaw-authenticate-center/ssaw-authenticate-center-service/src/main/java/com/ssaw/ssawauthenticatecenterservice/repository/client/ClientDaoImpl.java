package com.ssaw.ssawauthenticatecenterservice.repository.client;

import com.ssaw.ssawauthenticatecenterservice.entity.ClientDetailsEntity;
import com.ssaw.ssawauthenticatecenterservice.repository.ClientDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import javax.persistence.EntityManager;

/**
 * @author HuSen.
 * @date 2018/12/12 11:28.
 */
public class ClientDaoImpl extends SimpleJpaRepository<ClientDetailsEntity, String> implements ClientDao {

    public ClientDaoImpl(@Autowired EntityManager entityManager) {
        super(ClientDetailsEntity.class, entityManager);
        em = entityManager;
    }

    private final EntityManager em;
}