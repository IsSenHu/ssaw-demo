package com.ssaw.ssawmehelper;

import com.ssaw.ssawmehelper.dao.redis.KaoQinDao;
import com.ssaw.ssawmehelper.model.vo.kaoqin.IOnlineReqVO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Set;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SsawMeHelperApplicationTests {

    @Autowired
    private KaoQinDao kaoQinDao;

    @Test
    public void contextLoads() {
        IOnlineReqVO reqVO = new IOnlineReqVO();
        reqVO.setBn("123456");
        reqVO.setDutyTime("asdasdas");

        kaoQinDao.insertOnlineTime(reqVO);
        Set<String> strings = kaoQinDao.allOnlineTime("123456");
        System.out.println(strings);

        IOnlineReqVO reqVO1 = new IOnlineReqVO();
        reqVO1.setBn("123456");
        reqVO1.setDutyTime("asdasdass");

        kaoQinDao.insertOnlineTime(reqVO1);
        Set<String> stringSet = kaoQinDao.allOnlineTime("123456");
        System.out.println(stringSet);

        boolean b = kaoQinDao.deleteAllOnlineTime("123456");

        Set<String> stringSet1 = kaoQinDao.allOnlineTime("123456");
        System.out.println(stringSet1);
    }
}
