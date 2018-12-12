package com.ssaw.ssawauthenticatecenterservice.repository;

import com.ssaw.ssawauthenticatecenterservice.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author HuSen.
 * @date 2018/11/27 18:46.
 */
@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    UserEntity findByUsername(String username);

    Integer countByUsername(String username);
}
