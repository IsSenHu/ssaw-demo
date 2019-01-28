package com.ssaw.ssawauthenticatecenterservice.repository.user;

import com.ssaw.ssawauthenticatecenterservice.entity.UserRoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRoleEntity, Long> {
    UserRoleEntity findByUserId(Long userId);
}
