package com.ssaw.ssawmehelper;

import com.ssaw.ssawmehelper.dao.redis.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SsawMeHelperApplicationTests {

    @Autowired
    private KaoQinDao kaoQinDao;

    @Autowired
    private MyCollectionDao myCollectionDao;

    @Autowired
    private GoodsDemoDao goodsDemoDao;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private MarketDao marketDao;

    @Autowired
    private LogDao logDao;

    @Test
    public void contextLoads() {
        for (int i = 0; i < 1000; i++) {
            logDao.recent("日志" + i);
        }
    }
}
