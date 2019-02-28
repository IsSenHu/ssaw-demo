package com.ssaw.ssawauthenticatecenterservice.dao.repository.user;

import com.ssaw.ssawauthenticatecenterservice.dao.entity.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * @author HuSen.
 * @date 2018/11/27 18:46.
 */
@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long>, JpaSpecificationExecutor<UserEntity> {

    /**
     * 根据用户名查询用户
     * @param username 用户名
     * @return 用户
     */
    UserEntity findByUsername(String username);

    /**
     * 根据用户名计数
     * @param username 用户名
     * @return 数量
     */
    Integer countByUsername(String username);
}
