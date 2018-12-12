package com.ssaw.ssawuserresourceservice.repository.scope;

import com.ssaw.ssawuserresourceservice.entity.ScopeEntity;
import com.ssaw.ssawuserresourceservice.repository.ScopeDao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * @author HuSen.
 * @date 2018/12/12 10:35.
 */
@Repository
public interface ScopeRepository extends JpaRepository<ScopeEntity, Long>, JpaSpecificationExecutor<ScopeEntity>, ScopeDao {

}
