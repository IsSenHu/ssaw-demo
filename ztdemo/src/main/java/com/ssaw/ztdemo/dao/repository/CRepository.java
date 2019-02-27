package com.ssaw.ztdemo.dao.repository;

import com.ssaw.ztdemo.dao.po.CPO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author hszyp
 */
@Repository
public interface CRepository extends JpaRepository<CPO, Long> {

    CPO findByOrderBn(String orderBn);

}
