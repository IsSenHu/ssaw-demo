package com.ssaw.ssawuserresourcefeign.feign;

import com.ssaw.ssawuserresourcefeign.fallback.ClientFeignFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;

/**
 * @author HuSen.
 * @date 2018/12/11 14:34.
 */
@Component
@FeignClient(name = "ssaw-authenticate-center", path = "/api/client", decode404 = true, fallback = ClientFeignFallback.class)
public interface ClientFeign {

}
