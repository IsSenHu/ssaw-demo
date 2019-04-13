package com.ssaw.ssawmehelper.service.kaoqin.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.ssaw.commons.util.http.HttpConnectionUtils;
import com.ssaw.commons.vo.CommonResult;
import com.ssaw.commons.vo.PageReqVO;
import com.ssaw.commons.vo.TableData;
import com.ssaw.ssawauthenticatecenterfeign.util.UserUtils;
import com.ssaw.ssawmehelper.api.constants.KaoqinConstants;
import com.ssaw.ssawmehelper.dao.mapper.employee.EmployeeMapper;
import com.ssaw.ssawmehelper.dao.po.employee.EmployeePO;
import com.ssaw.ssawmehelper.dao.redis.KaoQinDao;
import com.ssaw.ssawmehelper.handler.CommitOverTimeHandler;
import com.ssaw.ssawmehelper.model.vo.kaoqin.*;
import com.ssaw.ssawmehelper.service.consumption.BaseService;
import com.ssaw.ssawmehelper.service.employee.EmployeeService;
import com.ssaw.ssawmehelper.service.kaoqin.KaoQinService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLDecoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.ssaw.commons.constant.Constants.ResultCodes.*;

/**
 * @author HuSen
 * @date 2019/3/20 15:22
 */
@Slf4j
@Service
public class KaoQinServiceImpl extends BaseService implements KaoQinService {

    private final EmployeeMapper employeeMapper;

    private final KaoQinDao kaoQinDao;

    @Resource(name = "commitOverTimeExecutor")
    private ThreadPoolTaskExecutor commitOverTimeExecutor;

    private final EmployeeService employeeService;

    private final CommitOverTimeHandler commitOverTimeHandler;

    @Autowired
    public KaoQinServiceImpl(EmployeeMapper employeeMapper, KaoQinDao kaoQinDao, CommitOverTimeHandler commitOverTimeHandler, EmployeeService employeeService) {
        this.employeeMapper = employeeMapper;
        this.kaoQinDao = kaoQinDao;
        this.commitOverTimeHandler = commitOverTimeHandler;
        this.employeeService = employeeService;
    }

    /**
     * 分页查询考勤信息
     *
     * @param pageReqVO 查询数据模型
     * @return 分页结果
     */
    @Override
    public TableData<KaoQinInfoVO> page(PageReqVO<KaoQinInfoQueryVO> pageReqVO) {
        TableData<KaoQinInfoVO> tableData = new TableData<>();
        PageReqVO<KaoQinInfoQueryVO> page = getPage(pageReqVO);
        tableData.setPage(page.getPage());
        tableData.setSize(page.getSize());
        KaoQinInfoQueryVO data = pageReqVO.getData();
        QueryWrapper<EmployeePO> queryWrapper = new QueryWrapper<EmployeePO>()
                .eq("username", UserUtils.getUser().getUsername());
        EmployeePO employeePO = employeeMapper.selectOne(queryWrapper);
        if (Objects.isNull(employeePO)) {
            tableData.setTotals(0L);
            tableData.setTotalPages(0);
            tableData.setContent(Lists.newArrayList());
        } else {
            List<KaoQinInfoVO> kaoQinInfoVOList = getKaoQinInfoVOList(data.getYear(), data.getMonth(), employeePO);
            Set<String> allOnlineTime = kaoQinDao.allOnlineTime(employeePO.getBn());
            Set<String> allForgetTime = kaoQinDao.allForgetTime(employeePO.getBn());
            Set<String> allCommitOverTime = kaoQinDao.allCommitOverTime(employeePO.getBn());

            kaoQinInfoVOList.forEach(k -> {
                String dutyDate = k.getDutyDate();
                k.setOnline(allOnlineTime.contains(dutyDate));
                k.setForget(allForgetTime.contains(dutyDate));
                k.setCommitOverTime(allCommitOverTime.contains(dutyDate));
            });

            tableData.setContent(kaoQinInfoVOList);
            tableData.setTotalPages(1);
            tableData.setTotals((long) kaoQinInfoVOList.size());
        }
        return tableData;
    }

    /**
     * 提交加班申请单
     *
     * @param reqVO 提交加班申请单数据模型
     * @return 申请结果
     */
    @Override
    public CommonResult<CommitOverTimeInfoReqVO> commitOverTimeInfo(CommitOverTimeInfoReqVO reqVO) {
        commitOverTimeExecutor.execute(() -> commitOverTimeHandler.work(reqVO));
        // 记录redis 该加班申请已提交过了
        kaoQinDao.insertCommitOverTime(reqVO);
        return CommonResult.createResult(SUCCESS, "提交成功，请等待执行结果", reqVO);
    }

    /**
     * 提交调休申请单
     *
     * @param reqVO 提交调休申请单数据模型
     * @return 申请结果
     */
    @Override
    public CommonResult<CommitLeaveReqVO> commitLeave(CommitLeaveReqVO reqVO) {
        JSONObject calLeaveTimeObj = new JSONObject();
        EmployeePO employeePO = employeeService.getEmployeePO(reqVO.getBn());
        if (Objects.isNull(employeePO)) {
            return CommonResult.createResult(DATA_NOT_EXIST, "该工作人员未录入该系统", reqVO);
        }
        calLeaveTimeObj.put("A0188", employeePO.getEhrBn());
        calLeaveTimeObj.put("A0188s", employeePO.getEhrBn());
        calLeaveTimeObj.put("LEAVE_BEGIN", reqVO.getBeginTime());
        calLeaveTimeObj.put("LEAVE_END", reqVO.getEndTime());
        calLeaveTimeObj.put("LEAVE_TYPE", "19");
        calLeaveTimeObj.put("ID", 0);
        calLeaveTimeObj.put("flag", 0);
        try {
            String leaveTime = HttpConnectionUtils.doPost("https://ehr.1919.cn/api/KQService/CalLeaveTime?ap=" + employeePO.getEhrAp(),
                    calLeaveTimeObj.toJSONString(), false);
            assert leaveTime != null;
            String errorMsg = ((JSONObject) JSON.parseArray(leaveTime).get(0)).getString("ErrorMsg");
            if (!Objects.equals(errorMsg, StringUtils.EMPTY)) {
                BigDecimal last = errorMsg.contains("期初") ? new BigDecimal(errorMsg.split("剩余")[2].split("小时")[0]) : BigDecimal.ZERO;
                BigDecimal over = new BigDecimal(errorMsg.split("加班单")[1].split("小时")[0]);
                BigDecimal leave = new BigDecimal(errorMsg.split("请假")[2].split("小时")[0]);
                return CommonResult.createResult(ERROR, "可调休时间剩余: " + last.add(over).subtract(leave) +
                        " 小时, 需要: " + reqVO.getLeaveTime().setScale(2, RoundingMode.HALF_DOWN) + " 小时", reqVO);
            }
            JSONObject jOriginalDataXml = JSON.parseObject(KaoqinConstants.TIAO_XIU_J_ORIGINAL_DATA_XML);
            JSONObject tables = JSON.parseObject(KaoqinConstants.TIAO_XIU_TABLES);
            JSONObject reqJsonObject = new JSONObject();
            reqJsonObject.put("updateTable", "K_LEAVE");
            reqJsonObject.put("primaryKey", "K_ID");
            reqJsonObject.put("userUpdateState", "0|保存");
            reqJsonObject.put("Tables", tables);
            JSONObject data = new JSONObject();
            data.put("K_LEAVE_K_ID", StringUtils.EMPTY);
            data.put("K_LEAVE_LEAVE_REASON", "调休");
            data.put("K_LEAVE_A0188", employeePO.getEhrBn());
            data.put("K_LEAVE_LEAVE_TYPE", "19");
            data.put("K_LEAVE_LEAVE_BEGIN", reqVO.getBeginTime());
            data.put("K_LEAVE_LEAVE_END", reqVO.getEndTime());
            data.put("K_LEAVE_LEAVE_TIME", reqVO.getLeaveTime());
            data.put("K_LEAVE_LEAVE_DAYS", reqVO.getLeaveDays());
            data.put("K_LEAVE_LEAVEFILE", StringUtils.EMPTY);
            data.put("K_LEAVE_SIGNED", "0");
            data.put("DataRightType", "0");
            data.put("ValidateState", "1");
            data.put("FormulaState", "1");
            data.put("SequenceID", "0");
            data.put("@RowState", "Added");
            JSONObject dataSet = new JSONObject();
            dataSet.put("DATA", data);
            JSONObject jDataXml = new JSONObject();
            jDataXml.put("DataSet", dataSet);
            reqJsonObject.put("JDataXML", jDataXml);
            reqJsonObject.put("JOriginalDataXML", jOriginalDataXml);
            String json = JSON.toJSONString(reqJsonObject);
            String result = HttpConnectionUtils.doPost("https://ehr.1919.cn/api/ComService/UpdateEx?ap=" + employeePO.getEhrAp(),
                    json, false);
            try {
                final String addedId = "AddedID";
                if (StringUtils.contains(result, addedId)) {
                    String signIds = result.split("<AddedID>")[1].split("</AddedID>")[0];
                    return CommonResult.createResult(SUCCESS, startWf(signIds, employeePO), reqVO);
                } else {
                    return CommonResult.createResult(SUCCESS, URLDecoder.decode(result.split("<Message>")[1].split("</Message>")[0], "utf-8"), reqVO);
                }
            } catch (Exception e) {
                log.error("保存数据失败，结果：{}", result);
                return CommonResult.createResult(ERROR, "保存数据库失败", reqVO);
            }
        } catch (Exception e) {
            log.error("执行失败:", e);
            return CommonResult.createResult(ERROR, "执行失败", reqVO);
        }
    }

    /**
     * 我上线了
     *
     * @param reqVO 确认我上线了
     * @return 确认结果
     */
    @Override
    public CommonResult<IOnlineReqVO> iOnline(IOnlineReqVO reqVO) {
        boolean success = kaoQinDao.insertOnlineTime(reqVO);
        if (success) {
            return CommonResult.createResult(SUCCESS, "确认我上线了", reqVO);
        } else {
            return CommonResult.createResult(ERROR, "确认我上线失败", reqVO);
        }
    }

    /**
     * 我忘记打卡了
     *
     * @param reqVO 确认我忘记打卡了
     * @return 确认结果
     */
    @Override
    public CommonResult<IForgetPlayCardReqVO> iForgetPlayCard(IForgetPlayCardReqVO reqVO) {
        boolean success = kaoQinDao.insertForgetTime(reqVO);
        if (success) {
            return CommonResult.createResult(SUCCESS, "确认我忘记打卡了", reqVO);
        } else {
            return CommonResult.createResult(ERROR, "确认我忘记打卡失败", reqVO);
        }
    }

    private List<KaoQinInfoVO> getKaoQinInfoVOList(Integer year, Integer month, EmployeePO employee) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month - 1);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        String fromDate = new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
        calendar.roll(Calendar.DAY_OF_MONTH, -1);
        String toDate = new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
        Map<String, String> paramMap = Maps.newHashMap();
        paramMap.put("A0188", employee.getEhrBn());
        paramMap.put("fromDate", fromDate);
        paramMap.put("toDate", toDate);
        paramMap.put("resultType", "(1=1)");
        try {
            String result = HttpConnectionUtils.doPost("https://ehr.1919.cn/api/KQService/QueryKQResult?ap=" + employee.getEhrAp(),
                    JSON.toJSONString(paramMap), false);
            JSONObject jsonObject = JSON.parseObject(result);
            String jDataXml = jsonObject.getString("JDataXML");
            String newDataSet = JSON.parseObject(jDataXml).getString("NewDataSet");
            String data = JSON.parseObject(newDataSet).getString("DATA");
            JSONArray dataJSONOArray = JSON.parseArray(data);
            Map<String, String> kqOverInfo = getKqOverInfo(fromDate, toDate, employee);
            Map<String, String> kqLeaveInfo = getKqLeaveInfo(fromDate, toDate, employee);
            try {
                for (Object dataJSONObject : dataJSONOArray.toArray()) {
                    String kDayDutyDate = ((JSONObject) dataJSONObject).getString("K_DAY_DUTY_DATE");
                    String dateString = simpleDateFormat.format(simpleDateFormat.parse(kDayDutyDate));
                    ((JSONObject) dataJSONObject).put("KqOverStatus", kqOverInfo.getOrDefault(dateString, "未提交"));
                    if (kqLeaveInfo.containsKey(dateString)) {
                        ((JSONObject) dataJSONObject).put("KqLeaveStatus", kqLeaveInfo.get(dateString));
                    } else if (StringUtils.contains(((JSONObject) dataJSONObject).getString("K_DAY_K_DAYSTATE"), "缺勤")
                            || StringUtils.contains(((JSONObject) dataJSONObject).getString("K_DAY_K_DAYSTATE"), "迟到")) {
                        ((JSONObject) dataJSONObject).put("KqLeaveStatus", "未提交");
                    } else {
                        ((JSONObject) dataJSONObject).put("KqLeaveStatus", "");
                    }
                }
            } catch (ParseException e) {
                log.error("解析日期发生错误", e);
            }
            return JSONArray.parseArray(dataJSONOArray.toJSONString(), KaoQinInfoVO.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Lists.newArrayList();
    }

    private Map<String, String> getKqOverInfo(String formDate, String toDate, EmployeePO employeePO) {
        Map<String, String> result = Maps.newHashMap();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(simpleDateFormat.parse(toDate));
            c.add(Calendar.DAY_OF_MONTH, 1);
            JSONObject kqOverJSONObject = new JSONObject();
            kqOverJSONObject.put("A0188", employeePO.getEhrBn());
            kqOverJSONObject.put("fromDate", formDate);
            kqOverJSONObject.put("toDate", simpleDateFormat.format(c.getTime()));
            kqOverJSONObject.put("spStatus", "4");
            String kqOver = HttpConnectionUtils.doPost("https://ehr.1919.cn/api/KQService/QueryKQOver?ap=" +
                    employeePO.getEhrAp(), kqOverJSONObject.toJSONString(), false);
            String kqOverJDataXml = JSON.parseObject(kqOver).getString("JDataXML");
            String kqOverNewDataSet = JSON.parseObject(kqOverJDataXml).getString("NewDataSet");
            if (kqOverNewDataSet != null) {
                String kqOverData = JSON.parseObject(kqOverNewDataSet).getString("DATA");
                final String left = "[";
                if (!StringUtils.contains(kqOverData, left)) {
                    JSONObject kqOverDataJSONObject = JSON.parseObject(kqOverData);
                    String kOverKOverrq = kqOverDataJSONObject.getString("K_OVER_K_OVERRQ");
                    if (StringUtils.isNotBlank(kOverKOverrq)) {
                        Date date = simpleDateFormat.parse(kOverKOverrq);
                        result.put(simpleDateFormat.format(date), kqOverDataJSONObject.getString("K_OVER_SIGNEDMC"));
                    }
                } else {
                    JSONArray kqOverDataJSONOArray = JSON.parseArray(kqOverData);
                    for (Object kqOverDataJSONObject : kqOverDataJSONOArray.toArray()) {
                        String kOverKOverrq = ((JSONObject) kqOverDataJSONObject).getString("K_OVER_K_OVERRQ");
                        if (kOverKOverrq != null) {
                            Date date = simpleDateFormat.parse(kOverKOverrq);
                            result.put(simpleDateFormat.format(date), ((JSONObject) kqOverDataJSONObject).getString("K_OVER_SIGNEDMC"));
                        }
                    }
                }
            }
        } catch (ParseException e) {
            log.error("解析日期发生错误", e);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    private Map<String, String> getKqLeaveInfo(String formDate, String toDate, EmployeePO employeePO) {
        Map<String, String> result = Maps.newHashMap();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(simpleDateFormat.parse(toDate));
            c.add(Calendar.DAY_OF_MONTH, 1);
            JSONObject kqLeaveJSONObject = new JSONObject();
            kqLeaveJSONObject.put("A0188", employeePO.getEhrBn());
            kqLeaveJSONObject.put("fromDate", formDate);
            kqLeaveJSONObject.put("toDate", simpleDateFormat.format(c.getTime()));
            kqLeaveJSONObject.put("spStatus", "4");
            String kqLeave;
            try {
                kqLeave = HttpConnectionUtils.doPost("https://ehr.1919.cn/api/KQService/QueryKQLeave?ap=" +
                        employeePO.getEhrAp(), kqLeaveJSONObject.toJSONString(), false);
                String kqLeaveJDataXml = JSON.parseObject(kqLeave).getString("JDataXML");
                String kqLeaveNewDataSet = JSON.parseObject(kqLeaveJDataXml).getString("NewDataSet");
                if (kqLeaveNewDataSet != null) {
                    String kqLeaveData = JSON.parseObject(kqLeaveNewDataSet).getString("DATA");
                    final String left = "[";
                    if (StringUtils.contains(kqLeaveData, left)) {
                        JSONArray kqLeaveDataJSONOArray = JSON.parseArray(kqLeaveData);
                        for (Object kqLeaveDataJSONObject : kqLeaveDataJSONOArray.toArray()) {
                            String leaveBegin = ((JSONObject) kqLeaveDataJSONObject).getString("K_LEAVE_LEAVE_BEGIN");
                            Date date = simpleDateFormat.parse(leaveBegin);
                            result.put(simpleDateFormat.format(date), ((JSONObject) kqLeaveDataJSONObject).getString("K_LEAVE_SIGNEDMC"));
                        }
                    } else {
                        JSONObject kqLeaveDataJSONObj = (JSONObject) JSON.parse(kqLeaveData);
                        String leaveBegin = kqLeaveDataJSONObj.getString("K_LEAVE_LEAVE_BEGIN");
                        Date date = simpleDateFormat.parse(leaveBegin);
                        result.put(simpleDateFormat.format(date), kqLeaveDataJSONObj.getString("K_LEAVE_SIGNEDMC"));
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (ParseException e) {
            log.error("解析日期发生错误", e);
        }
        return result;
    }

    private String startWf(String signIds, EmployeePO employee) throws IOException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("spbm", "050102");
        jsonObject.put("signIDs", signIds);
        jsonObject.put("A0188", employee.getEhrBn());
        return HttpConnectionUtils.doPost("https://ehr.1919.cn/api/WFService/StartWF?ap=" + employee.getEhrAp(),
                jsonObject.toJSONString(), false);
    }

    private int getMonth(int i) {
        switch (i) {
            case 0: {
                return Calendar.JANUARY;
            }
            case 1: {
                return Calendar.FEBRUARY;
            }
            case 2: {
                return Calendar.MARCH;
            }
            case 3: {
                return Calendar.APRIL;
            }
            case 4: {
                return Calendar.MAY;
            }
            case 5: {
                return Calendar.JUNE;
            }
            case 6: {
                return Calendar.JULY;
            }
            case 7: {
                return Calendar.AUGUST;
            }
            case 8: {
                return Calendar.SEPTEMBER;
            }
            case 9: {
                return Calendar.OCTOBER;
            }
            case 10: {
                return Calendar.NOVEMBER;
            }
            case 11: {
                return Calendar.DECEMBER;
            }
            default: {
                return Calendar.JANUARY;
            }
        }
    }
}