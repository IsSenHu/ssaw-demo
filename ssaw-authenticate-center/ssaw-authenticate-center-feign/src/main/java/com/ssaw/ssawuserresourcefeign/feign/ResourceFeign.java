package com.ssaw.ssawuserresourcefeign.feign;

import com.ssaw.ssawuserresourcefeign.fallback.ResourceFeignFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;

/**
 * @author HuSen.
 * @date 2018/12/11 14:08.
 */
@Component
@FeignClient(name = "ssaw-authenticate-center", path = "/api/resource", decode404 = true, fallback = ResourceFeignFallback.class)
public interface ResourceFeign {

}
