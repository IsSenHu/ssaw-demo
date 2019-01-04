package com.ssaw.ssawuserresourcefeign.feign;

import com.ssaw.commons.vo.CommonResult;
import com.ssaw.commons.vo.PageReqDto;
import com.ssaw.commons.vo.TableData;
import com.ssaw.ssawuserresourcefeign.dto.UserDto;
import com.ssaw.ssawuserresourcefeign.fallback.UserFeignFallBack;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import javax.validation.Valid;
import java.util.List;

/**
 * @author HuSen.
 * @date 2018/11/27 18:59.
 */
@Component
@FeignClient(name = "ssaw-user-server", path = "/api/user", fallback = UserFeignFallBack.class)
public interface UserFeign {

    @GetMapping("/get/{username}")
    CommonResult<UserDto> findByUsername(@PathVariable(name = "username") String username);

    @PostMapping("/save")
    CommonResult<UserDto> save(@RequestBody @Valid UserDto userDto);

    @PostMapping("/delete/{userId}")
    CommonResult<Long> delete(@PathVariable(name = "userId") Long userId);

    @PostMapping("/saveUserRoles/{userId}")
    CommonResult<Long> saveUserRoles(@PathVariable(name = "userId") Long userId, @RequestBody List<Long> roleIds);

    @PostMapping("/page")
    TableData<UserDto> page(@RequestBody PageReqDto<UserDto> pageReq);
}
