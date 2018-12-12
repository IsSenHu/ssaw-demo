package com.ssaw.ssawauthenticatecenterservice.repository.client;

import com.ssaw.ssawauthenticatecenterservice.entity.ClientDetailsEntity;
import com.ssaw.ssawauthenticatecenterservice.repository.ClientDao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * @author HuSen.
 * @date 2018/12/11 9:34.
 */
@Repository
public interface ClientRepository extends JpaRepository<ClientDetailsEntity, String>, JpaSpecificationExecutor<ClientDetailsEntity>, ClientDao {

}
