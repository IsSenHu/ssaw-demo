package com.ssaw.ssawuserresourceservice.repository;

import com.ssaw.ssawuserresourceservice.entity.ResourceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author HuSen.
 * @date 2018/12/10 20:09.
 */
@Repository
public interface ResourceRepository extends JpaRepository<ResourceEntity, Integer> {

}
