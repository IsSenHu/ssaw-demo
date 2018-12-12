package com.ssaw.ssawauthenticatecenterservice.repository;

import com.ssaw.ssawauthenticatecenterservice.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author HuSen.
 * @date 2018/11/27 18:47.
 */
@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, Long> {

    List<RoleEntity> findAllByIdIn(List<Long> roleIds);

    Integer countByUniqueMark(String uniqueMark);
}
