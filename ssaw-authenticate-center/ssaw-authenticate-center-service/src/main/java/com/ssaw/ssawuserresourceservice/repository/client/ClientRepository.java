package com.ssaw.ssawuserresourceservice.repository.client;

import com.ssaw.ssawuserresourceservice.entity.ClientDetailsEntity;
import com.ssaw.ssawuserresourceservice.repository.ClientDao;
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
