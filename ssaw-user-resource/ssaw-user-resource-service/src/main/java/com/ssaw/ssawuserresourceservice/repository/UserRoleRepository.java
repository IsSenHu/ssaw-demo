package com.ssaw.ssawuserresourceservice.repository;

import com.ssaw.ssawuserresourceservice.entity.UserRoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * @author HuSen.
 * @date 2018/11/27 19:55.
 */
@Repository
public interface UserRoleRepository extends JpaRepository<UserRoleEntity, Long> {

    void deleteAllByUserId(Long userId);

    void deleteAllByRoleId(Long roleId);

    List<UserRoleEntity> findAllByUserId(Long userId);
}
