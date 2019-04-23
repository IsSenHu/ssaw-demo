package com.ssaw.ssawmehelper.service.collection.impl;

import com.ssaw.commons.util.bean.CopyUtil;
import com.ssaw.commons.vo.CommonResult;
import com.ssaw.commons.vo.TableData;
import com.ssaw.ssawauthenticatecenterfeign.util.UserUtils;
import com.ssaw.ssawmehelper.dao.redis.MyCollectionDao;
import com.ssaw.ssawmehelper.dao.ro.MyCollectionRO;
import com.ssaw.ssawmehelper.model.vo.collection.MyCollectionCreateRequestVO;
import com.ssaw.ssawmehelper.model.vo.collection.MyCollectionVO;
import com.ssaw.ssawmehelper.service.collection.MyCollectionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.ssaw.commons.constant.Constants.ResultCodes.SUCCESS;

/**
 * @author HuSen
 * @date 2019/4/19 14:16
 */
@Slf4j
@Service
public class MyCollectionServiceImpl implements MyCollectionService {

    private final MyCollectionDao myCollectionDao;

    @Autowired
    public MyCollectionServiceImpl(MyCollectionDao myCollectionDao) {
        this.myCollectionDao = myCollectionDao;
    }

    /**
     * 新增收藏类别
     *
     * @param classification 收藏类别
     * @return 新增结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult<String> newClassification(String classification) {
        myCollectionDao.newClassification(classification);
        return CommonResult.createResult(SUCCESS, "成功", classification);
    }

    /**
     * 获取所有收藏类别
     *
     * @return 所有收藏类别
     */
    @Override
    public CommonResult<Set<String>> allClassification() {
        return CommonResult.createResult(SUCCESS, "成功", myCollectionDao.allClassification());
    }

    /**
     * 创建我的收藏
     *
     * @param requestVO 创建我的收藏请求数据模型
     * @return 创建结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult<MyCollectionCreateRequestVO> create(MyCollectionCreateRequestVO requestVO) {
        MyCollectionRO ro = new MyCollectionRO();
        Long userId = UserUtils.getUser().getId();
        ro.setUserId(userId);
        ro.setTitle(requestVO.getTitle());
        ro.setLink(requestVO.getLink());
        ro.setDesc(requestVO.getDesc());
        long time = System.currentTimeMillis();
        ro.setTime(time);
        ro.setVotes(0);
        ro.setClassification(requestVO.getClassification());
        String key = myCollectionDao.insert(ro);
        Long timeScore = myCollectionDao.insertTimeScore(key, time);
        Long edenScore = myCollectionDao.insertScore(key, time);
        log.info("创建我的收藏成功:{} - {} - {}", key, timeScore, edenScore);
        return CommonResult.createResult(SUCCESS, "成功", requestVO);
    }

    /**
     * 增加收藏分数
     *
     * @param id 收藏ID
     * @return 操作结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult<Long> addScore(Long id) {
        String key = myCollectionDao.idToKey(id);
        Double score = myCollectionDao.addScore(key);
        log.info("增加收藏分数成功{} - {}", key, score);
        return CommonResult.createResult(SUCCESS, "成功", id);
    }

    /**
     * 收藏列表
     *
     * @param byTime 是否通过时间排序
     * @return 收藏列表
     */
    @Override
    public TableData<MyCollectionVO> list(boolean byTime) {
        List<MyCollectionRO> ros;
        if (byTime) {
            ros = myCollectionDao.getFirstTenByTime();
        } else {
            ros = myCollectionDao.getFirstTenByScore();
        }
        TableData<MyCollectionVO> tableData = new TableData<>();
        tableData.setPage(1);
        tableData.setSize(10);
        tableData.setTotals((long) ros.size());
        tableData.setTotalPages(1);
        tableData.setContent(ros.stream().map(input -> CopyUtil.copyProperties(input, new MyCollectionVO())).collect(Collectors.toList()));
        return tableData;
    }
}