package com.ssaw.ssawmehelper.dao.mapper.consumption;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ssaw.ssawmehelper.dao.po.consumption.MyConsumptionPO;
import com.ssaw.ssawmehelper.model.vo.consumption.MyConsumptionQueryVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * @author HuSen
 * @date 2019/3/16 12:20
 */
@Repository
public interface MyConsumptionMapper extends BaseMapper<MyConsumptionPO> {

    /**
     * 批量保存我的收入
     * @param myConsumptionPOS 我的收入数据集合
     */
    void saveAll(List<MyConsumptionPO> myConsumptionPOS);

    /**
     * 根据消费日期查询我的消费记录
     * @param costDate 消费日期
     * @param username 用户名
     * @return 我的消费记录
     */
    MyConsumptionPO findByCostDateAndUsername(@Param("costDate") LocalDate costDate, @Param("username") String username);

    /**
     * 分页查询我的消费
     * @param page 分页数据模型
     * @param data 查询条件数据模型
     * @param username
     * @return 分页结果
     */
    Page<MyConsumptionPO> findAll(IPage<MyConsumptionPO> page, @Param("query") MyConsumptionQueryVO data, @Param("username") String username);

    /**
     * 根据用户名，开始时间，结束时间查询我的消费
     * @param username 用户名
     * @param start 开始时间
     * @param end 结束时间
     * @return 我的消费
     */
    List<MyConsumptionPO> findAllByUsernameAndStartAndEnd(@Param("username") String username, @Param("start") String start, @Param("end") String end);
}
