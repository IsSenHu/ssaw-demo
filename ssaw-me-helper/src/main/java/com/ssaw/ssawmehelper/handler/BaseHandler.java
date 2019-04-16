package com.ssaw.ssawmehelper.handler;

import com.alibaba.fastjson.JSONObject;
import com.ssaw.commons.util.http.HttpConnectionUtils;
import com.ssaw.ssawmehelper.dao.po.employee.EmployeePO;
import lombok.extern.slf4j.Slf4j;

import java.util.Calendar;

/**
 * @author HuSen
 * @date 2019/4/16 9:31
 */
@Slf4j
public class BaseHandler {
    int getMonth(int i) {
        switch (i) {
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

     String startWf(String spbm, String signIds, EmployeePO employee) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("spbm", spbm);
            jsonObject.put("signIDs", signIds);
            jsonObject.put("A0188", employee.getEhrBn());
            return HttpConnectionUtils.doPost("https://ehr.1919.cn/api/WFService/StartWF?ap=" + employee.getEhrAp(),
                    jsonObject.toJSONString(), false);
            // 考虑要不要添加审批重试机制
        } catch (Exception e) {
            log.error("提交审批失败:", e);
            return null;
        }
    }
}