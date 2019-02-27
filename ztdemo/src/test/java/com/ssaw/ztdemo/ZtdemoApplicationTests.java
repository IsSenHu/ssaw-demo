package com.ssaw.ztdemo;

import com.alibaba.fastjson.JSON;
import com.ssaw.commons.util.http.HttpConnectionUtils;
import com.ssaw.commons.util.poi.ExcelCVo;
import com.ssaw.commons.util.poi.ExcelUtils;
import com.ssaw.commons.vo.CommonResult;
import com.ssaw.ztdemo.dao.po.CPO;
import com.ssaw.ztdemo.dao.repository.CRepository;
import com.ssaw.ztdemo.vo.DeliveryAppOrderDto;
import com.ssaw.ztdemo.vo.DeliveryAppOrderReqVo;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.message.BasicHeader;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.apache.http.entity.ContentType;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ZtdemoApplicationTests {

    @Autowired
    private CRepository cRepository;

    @Test
    public void contextLoads() throws IOException, IllegalAccessException {
        Header[] HEADERS = {new BasicHeader("accept", "*/*"), new BasicHeader("connection", "Keep-Alive")};

        List<String> orderBns = ExcelUtils.read("C:\\Users\\hszyp\\Desktop\\美团取消订单-经纬度查询.xlsx");

        List<ExcelCVo> collect = orderBns.stream().map(c -> {
            System.out.println(c);
            ExcelCVo excelCVo = new ExcelCVo();
            excelCVo.setOrderBn(c);
            CPO cpo = cRepository.findByOrderBn(c);
            if (null != cpo) {
                if (StringUtils.isNotBlank(cpo.getLongitude()) && StringUtils.isNotBlank(cpo.getLatitude())) {
                    excelCVo.setKuaiheDis(cpo.getLongitude().concat(", ").concat(cpo.getLatitude()));
                }
            }
            String timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
            String string = "shopBnb2capptimestamp" + timestamp;
            StringBuilder stringBuilder = new StringBuilder("http://cp.1919.cn/centerDocker" + "/api/order/findDeliverAppOrderByOrderBn?");
            stringBuilder.append("shopBn=b2capp").append("&")
                    .append("timestamp=").append(timestamp).append("&")
                    .append("sign=").append(com.biz.commondocker.util.Md5Util.GetMD5Code(string));
            DeliveryAppOrderReqVo reqVo = new DeliveryAppOrderReqVo();
            reqVo.setOrderBn(c);
            try {
                String resp = HttpConnectionUtils.doPost(stringBuilder.toString(), JSON.toJSONString(reqVo), true, ContentType.create("application/json", "utf-8"), HEADERS);
                if (StringUtils.isNotBlank(resp)) {
                    CommonResult deliveryAppOrderDto = JSON.parseObject(resp, CommonResult.class);
                    if (deliveryAppOrderDto.getCode() == 0) {
                        DeliveryAppOrderDto deliveryAppOrderDto1 = JSON.parseObject(JSON.toJSONString(deliveryAppOrderDto.getData()), DeliveryAppOrderDto.class);
                        if (StringUtils.isNotBlank(deliveryAppOrderDto1.getReceiverLng()) && StringUtils.isNotBlank(deliveryAppOrderDto1.getReceiverLat())) {
                            excelCVo.setToDeliveryAppDis(deliveryAppOrderDto1.getReceiverLng().concat(", ").concat(deliveryAppOrderDto1.getReceiverLat()));
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
            return excelCVo;
        }).collect(Collectors.toList());

        ExcelUtils.export(collect, ExcelCVo.class, new FileOutputStream(new File("C:\\Users\\hszyp\\Desktop\\Excel.xlsx")), "导出");

    }

}
