package com.ssaw.ssawconsumerdemo.feign.fallback;

import com.ssaw.ssawconsumerdemo.feign.DemoFeign;
import org.springframework.stereotype.Component;
import java.util.HashMap;
import java.util.Map;

/**
 * @author HuSen.
 * @date 2018/11/14 11:31.
 */
@Component
public class DemoFeignFallback implements DemoFeign {
    @Override
    public Map get() {
        return new HashMap(0);
    }
}
