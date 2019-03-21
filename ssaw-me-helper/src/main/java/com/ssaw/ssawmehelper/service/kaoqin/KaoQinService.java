package com.ssaw.ssawmehelper.service.kaoqin;

import com.ssaw.commons.vo.CommonResult;
import com.ssaw.commons.vo.PageReqVO;
import com.ssaw.commons.vo.TableData;
import com.ssaw.ssawmehelper.model.vo.kaoqin.CommitLeaveReqVO;
import com.ssaw.ssawmehelper.model.vo.kaoqin.CommitOverTimeInfoReqVO;
import com.ssaw.ssawmehelper.model.vo.kaoqin.KaoQinInfoQueryVO;
import com.ssaw.ssawmehelper.model.vo.kaoqin.KaoQinInfoVO;

/**
 * @author HuSen
 * @date 2019/3/20 15:22
 */
public interface KaoQinService {

    /**
     * 分页查询考勤信息
     * @param pageReqVO 查询数据模型
     * @return 分页结果
     */
    TableData<KaoQinInfoVO> page(PageReqVO<KaoQinInfoQueryVO> pageReqVO);

    /**
     * 提交加班申请单
     * @param reqVO 提交加班申请单数据模型
     * @return 申请结果
     */
    CommonResult<CommitOverTimeInfoReqVO> commitOverTimeInfo(CommitOverTimeInfoReqVO reqVO);

    /**
     * 提交调休申请单
     * @param reqVO 提交调休申请单数据模型
     * @return 申请结果
     */
    CommonResult<CommitLeaveReqVO> commitLeave(CommitLeaveReqVO reqVO);
}
