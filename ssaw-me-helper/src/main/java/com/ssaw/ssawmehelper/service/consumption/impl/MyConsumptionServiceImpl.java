package com.ssaw.ssawmehelper.service.consumption.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ssaw.commons.constant.Constants;
import com.ssaw.commons.util.bean.CopyUtil;
import com.ssaw.commons.vo.CommonResult;
import com.ssaw.commons.vo.PageReqVO;
import com.ssaw.commons.vo.TableData;
import com.ssaw.ssawauthenticatecenterfeign.util.UserUtils;
import com.ssaw.ssawmehelper.dao.mapper.consumption.MyConsumptionMapper;
import com.ssaw.ssawmehelper.dao.po.consumption.MyConsumptionPO;
import com.ssaw.ssawmehelper.service.consumption.BaseService;
import com.ssaw.ssawmehelper.service.consumption.MyConsumptionService;
import com.ssaw.ssawmehelper.model.vo.consumption.MyConsumptionQueryVO;
import com.ssaw.ssawmehelper.model.vo.consumption.MyConsumptionVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author HuSen
 * @date 2019/3/16 13:24
 */
@Service
public class MyConsumptionServiceImpl extends BaseService implements MyConsumptionService {

    private final MyConsumptionMapper myConsumptionMapper;

    @Autowired
    public MyConsumptionServiceImpl(MyConsumptionMapper myConsumptionMapper) {
        this.myConsumptionMapper = myConsumptionMapper;
    }

    /**
     * 批量保存我的收入
     * @param myConsumptionPOS 我的收入数据集合
     * @return 保存结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult saveAll(List<MyConsumptionPO> myConsumptionPOS) {
        if (CollectionUtils.isEmpty(myConsumptionPOS)) {
            return CommonResult.createResult(Constants.ResultCodes.PARAM_ERROR, "导入数据为空", null);
        }
        for (MyConsumptionPO myConsumptionPO : myConsumptionPOS) {
            MyConsumptionPO po = myConsumptionMapper.findByCostDateAndUsername(myConsumptionPO.getCostDate(), UserUtils.getUser().getUsername());
            if (Objects.nonNull(po)) {
                myConsumptionPOS.remove(myConsumptionPO);
                po.setExpenditure(myConsumptionPO.getExpenditure());
                po.setIncome(myConsumptionPO.getIncome());
                po.setNetExpenditure(myConsumptionPO.getNetExpenditure());
                myConsumptionMapper.updateById(po);
            }
        }
        myConsumptionMapper.saveAll(myConsumptionPOS);
        return CommonResult.createResult(Constants.ResultCodes.SUCCESS, "导入成功", null);
    }

    /**
     * 分页查询我的消费
     * @param pageReqVO 分页查询数据模型
     * @return 分页结果
     */
    @Override
    public TableData<MyConsumptionVO> page(PageReqVO<MyConsumptionQueryVO> pageReqVO) {
        pageReqVO = getPage(pageReqVO);
        IPage<MyConsumptionPO> iPage = new Page<MyConsumptionPO>()
                .setCurrent(pageReqVO.getPage()).setSize(pageReqVO.getSize()).setDesc("cost_date");
        iPage = myConsumptionMapper.findAll(iPage, pageReqVO.getData(), UserUtils.getUser().getUsername());
        TableData<MyConsumptionVO> tableData = new TableData<>();
        tableData.setPage(pageReqVO.getPage());
        tableData.setSize(pageReqVO.getSize());
        tableData.setTotals(iPage.getTotal());
        tableData.setTotalPages((int) iPage.getPages());
        List<MyConsumptionVO> data = iPage.getRecords().stream().map(input -> CopyUtil.copyProperties(input, new MyConsumptionVO())).collect(Collectors.toList());
        tableData.setContent(data);
        return tableData;
    }
}