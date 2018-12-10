package com.ssaw.ssawuserresourceservice.repository;

import com.ssaw.ssawuserresourceservice.entity.ServiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author HuSen.
 * @date 2018/12/10 20:10.
 */
@Repository
public interface ServiceRepository extends JpaRepository<ServiceEntity, Integer> {

}
