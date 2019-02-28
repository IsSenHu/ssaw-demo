package com.ssaw.ssawauthenticatecenterservice.dao.repository.system;

import com.ssaw.ssawauthenticatecenterservice.dao.entity.system.SystemEntity;
import com.ssaw.ssawauthenticatecenterservice.dao.repository.SystemDao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * @author HuSen
 * @date 2019/2/28 17:10
 */
@Repository
public interface SystemRepository extends JpaRepository<SystemEntity, Long>, JpaSpecificationExecutor<SystemEntity>, SystemDao {

}