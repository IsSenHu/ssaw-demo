package com.ssaw.ssawauthenticatecenterservice.repository.scope;

import com.ssaw.ssawauthenticatecenterservice.entity.ScopeEntity;
import com.ssaw.ssawauthenticatecenterservice.repository.ScopeDao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * @author HuSen.
 * @date 2018/12/12 10:35.
 */
@Repository
public interface ScopeRepository extends JpaRepository<ScopeEntity, Long>, JpaSpecificationExecutor<ScopeEntity>, ScopeDao {
    long countByScopeOrUri(String scope, String uri);

    long countByScope(String scope);

    long countByUri(String uri);

    Page<ScopeEntity> findAllByScopeLikeAndPermissionIdIsNull (String scope, Pageable pageable);

    Page<ScopeEntity> findAllByScopeLike (String scope, Pageable pageable);

    Page<ScopeEntity> findAllByPermissionIdIsNull(Pageable pageable);
}
