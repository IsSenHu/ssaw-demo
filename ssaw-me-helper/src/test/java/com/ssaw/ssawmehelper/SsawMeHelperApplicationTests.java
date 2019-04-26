package com.ssaw.ssawmehelper;

import com.ssaw.ssawmehelper.dao.redis.GoodsDemoDao;
import com.ssaw.ssawmehelper.dao.redis.MyCollectionDao;
import com.ssaw.ssawmehelper.dao.redis.KaoQinDao;
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

    @Test
    public void contextLoads() throws InterruptedException {
//        for (long i = 0; i < 1000L; i++) {
//            Thread.sleep(1);
//            goodsDemoDao.insertViewedUser(i);
//        }
         stringRedisTemplate.opsForZSet().removeRange("viewed_user", 0L, -2);
    }
}
