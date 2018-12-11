package com.ssaw.ssawuserresourceservice.repository;

import com.ssaw.ssawuserresourceservice.entity.ClientDetailsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author HuSen.
 * @date 2018/12/11 9:34.
 */
@Repository
public interface ClientRepository extends JpaRepository<ClientDetailsEntity, String> {

}
