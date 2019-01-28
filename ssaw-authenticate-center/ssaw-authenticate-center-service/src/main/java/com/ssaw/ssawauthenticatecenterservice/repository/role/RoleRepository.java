package com.ssaw.ssawauthenticatecenterservice.repository.role;

import com.ssaw.ssawauthenticatecenterservice.entity.RoleEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * @author HuSen.
 * @date 2018/12/14 17:51.
 */
@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, Long>, JpaSpecificationExecutor<RoleEntity> {

    long countByName(String name);

    Page<RoleEntity> findAllByNameLike(String name, Pageable pageable);
}
