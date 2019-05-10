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

    @Autowired
    private AutoCompletionDao autoCompletionDao;

    @Autowired
    private LockDao lockDao;

    @Test
    public void contextLoads() {
        marketDao.watch("胡森");
    }

    @Test
    public void demo2() {
        marketDao.watch("你好");
    }

    @Test
    public void demo3() {
        autoCompletionDao.get("ac");
    }

    @Test
    public void demo4() throws InterruptedException {
        lockDao.lock("husen");
        lockDao.unlock("husen");
    }

    @Test
    public void demo5() throws InterruptedException {
        lockDao.lock("husen");
        lockDao.unlock("husen");
    }
}
