package com.ssaw.ssawuserresourcefeign.feign;

import com.ssaw.ssawuserresourcefeign.fallback.ServiceFeignFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;

/**
 * @author HuSen.
 * @date 2018/12/11 13:43.
 */
@Component
@FeignClient(name = "ssaw-authenticate-center", path = "/api/service", decode404 = true, fallback = ServiceFeignFallback.class)
public interface ServiceFeign {

}
