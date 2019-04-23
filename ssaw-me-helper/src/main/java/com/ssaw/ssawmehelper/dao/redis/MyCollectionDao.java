package com.ssaw.ssawmehelper.dao.redis;

import com.google.common.collect.Lists;
import com.ssaw.commons.id.DefaultIdService;
import com.ssaw.commons.util.json.jack.JsonUtils;
import com.ssaw.ssawmehelper.dao.ro.MyCollectionRO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author HuSen
 * @date 2019/4/17 17:34
 */
@Slf4j
@Repository
public class MyCollectionDao {

    private final DefaultIdService defaultIdService;

    private final StringRedisTemplate stringRedisTemplate;

    private static final String COLLECTION_PREFIX = "collection:";

    private static final String ID_GENERATION_OFFSET = "id_generation_offset";

    private static final String COLLECTION_TIME_SCORE = "collection_time_score";

    private static final String COLLECTION_SCORE = "collection_score";

    private static final String CLASSIFICATION = "classification_set";

    private static final long ONE_TIME_SCORE = 86400000;

    public String idToKey(Long id) {
        return COLLECTION_PREFIX.concat(id.toString());
    }

    @Autowired
    public MyCollectionDao(StringRedisTemplate stringRedisTemplate, DefaultIdService defaultIdService) {
        this.stringRedisTemplate = stringRedisTemplate;
        this.defaultIdService = defaultIdService;
    }

    public String insert(MyCollectionRO ro) {
        try {
            Long id = defaultIdService.genId();
            if (Objects.isNull(id)) {
                return null;
            }
            ro.setId(id);

            log.info("插入收藏:{}", JsonUtils.object2JsonString(ro));
            Map<String, String> map = new HashMap<>(8);
            map.put("id", ro.getId().toString());
            map.put("userId", Objects.isNull(ro.getUserId()) ? "" : ro.getUserId().toString());
            map.put("title", ro.getTitle());
            map.put("link", ro.getLink());
            map.put("desc", ro.getDesc());
            map.put("classification", ro.getClassification());
            map.put("time", ro.getTime().toString());
            map.put("votes", ro.getVotes().toString());

            HashOperations<String, String, String> hash = stringRedisTemplate.opsForHash();
            String key = COLLECTION_PREFIX.concat(id.toString());
            hash.putAll(key, map);
            return key;
        } catch (Exception e) {
            log.error("插入收藏失败:", e);
            throw new RuntimeException("插入收藏失败");
        }
    }

    public Long insertTimeScore(String key, Long unix) {
        try {
            ZSetOperations<String, String> zSet = stringRedisTemplate.opsForZSet();
            zSet.add(COLLECTION_TIME_SCORE, key, unix);
            return unix;
        } catch (Exception e) {
            log.error("插入收藏时间分数失败:", e);
            throw new RuntimeException("插入收藏时间分数失败");
        }
    }

    public Long insertScore(String key, Long unix) {
        try {
            ZSetOperations<String, String> zSet = stringRedisTemplate.opsForZSet();
            zSet.add(COLLECTION_SCORE, key, unix);
            return unix;
        } catch (Exception e) {
            log.error("插入收藏分数失败:", e);
            throw new RuntimeException("插入收藏分数失败");
        }
    }

    private MyCollectionRO findOne(String key) {
        try {
            HashOperations<String, String, String> hash = stringRedisTemplate.opsForHash();
            Map<String, String> entries = hash.entries(key);
            return JsonUtils.jsonString2Object(JsonUtils.object2JsonString(entries), MyCollectionRO.class);
        } catch (Exception e) {
            log.error("根据key查询收藏失败:", e);
            return null;
        }
    }

    public Double addScore(String key) {
        try {
            ZSetOperations<String, String> zSet = stringRedisTemplate.opsForZSet();
            return zSet.incrementScore(COLLECTION_SCORE, key, ONE_TIME_SCORE);
        } catch (Exception e) {
            log.error("给Key新增分数失败:", e);
            throw new RuntimeException("给Key新增分数失败");
        }
    }

    /**
     * 获取分数排在前十的收藏
     *
     * @return 分数排在前十的收藏
     */
    public List<MyCollectionRO> getFirstTenByScore() {
        try {
            ZSetOperations<String, String> zSet = stringRedisTemplate.opsForZSet();
            Set<String> keys = zSet.reverseRangeByScore(COLLECTION_SCORE, Double.MIN_VALUE, Double.MAX_VALUE, 0, 10);
            Assert.notNull(keys, "获取分数排在前十的收藏的键集合为空");
            return keys.stream().map(this::findOne).collect(Collectors.toList());
        } catch (Exception e) {
            log.error("获取分数排在前十的收藏:", e);
            return Lists.newArrayList();
        }
    }

    /**
     * 获取最近发布的前十的收藏
     * @return 最近发布的前十的收藏
     */
    public List<MyCollectionRO> getFirstTenByTime() {
        try {
            ZSetOperations<String, String> zSet = stringRedisTemplate.opsForZSet();
            Set<String> keys = zSet.reverseRangeByScore(COLLECTION_TIME_SCORE, Double.MIN_VALUE, Double.MAX_VALUE, 0, 10);
            Assert.notNull(keys, "获取分数排在前十的收藏的键集合为空");
            return keys.stream().map(this::findOne).collect(Collectors.toList());
        } catch (Exception e) {
            log.error("获取分数排在前十的收藏:", e);
            return Lists.newArrayList();
        }
    }

    /**
     * 新增收藏类别
     *
     * @param classification 收藏类别
     */
    public void newClassification(String classification) {
        SetOperations<String, String> zet = stringRedisTemplate.opsForSet();
        zet.add(CLASSIFICATION, classification);
    }

    /**
     * 获取所有收藏类别
     *
     * @return 所有收藏类别
     */
    public Set<String> allClassification() {
        return stringRedisTemplate.opsForSet().members(CLASSIFICATION);
    }
}