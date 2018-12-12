package com.ssaw.ssawuserresourceservice.repository.resource;

import com.ssaw.ssawuserresourceservice.entity.ResourceEntity;
import com.ssaw.ssawuserresourceservice.repository.ResourceDao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * @author HuSen.
 * @date 2018/12/12 10:34.
 */
@Repository
public interface ResourceRepository extends JpaRepository<ResourceEntity, Long>, JpaSpecificationExecutor<ResourceEntity>, ResourceDao {

}
