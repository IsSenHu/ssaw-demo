package com.ssaw.ssawuserresourcefeign.fallback;

import com.ssaw.commons.vo.CommonResult;
import com.ssaw.commons.vo.PageReqDto;
import com.ssaw.commons.vo.TableData;
import com.ssaw.ssawuserresourcefeign.dto.UserDto;
import com.ssaw.ssawuserresourcefeign.feign.UserFeign;
import org.springframework.stereotype.Component;
import javax.validation.Valid;
import java.util.List;
import static com.ssaw.commons.constant.Constants.ResultCodes.*;

/**
 * @author HuSen.
 * @date 2018/12/4 19:41.
 */
@Component
public class UserFeignFallBack implements UserFeign {
    @Override
    public CommonResult<UserDto> findByUsername(String username) {
        return CommonResult.createResult(ERROR, "服务降级", null);
    }

    @Override
    public CommonResult<UserDto> save(@Valid UserDto userDto) {
        return CommonResult.createResult(ERROR, "服务降级", userDto);
    }

    @Override
    public CommonResult<Long> delete(Long userId) {
        return CommonResult.createResult(ERROR, "服务降级", userId);
    }

    @Override
    public CommonResult<Long> saveUserRoles(Long userId, List<Long> roleIds) {
        return CommonResult.createResult(ERROR, "服务降级", userId);
    }

    @Override
    public TableData<UserDto> page(PageReqDto<UserDto> pageReq) {
        return new TableData<>();
    }
}
