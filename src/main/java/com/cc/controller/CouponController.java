package com.cc.controller;

import com.cc.domain.CouponRawData;
import com.cc.domain.TestRawData;
import com.cc.model.ModelPredictionTemplate;
import com.cc.service.CouponService;
import com.cc.service.TestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author caichengjie
 * @date 2022/3/29
 */
@Slf4j
@RestController
@RequestMapping("/coupon")
public class CouponController{

    @Autowired
    @Qualifier("couponService")
    private CouponService couponService;

    @RequestMapping("/predict")
    public String predict() {
        List<CouponRawData> couponRawDataList = new ArrayList<>();
        CouponRawData couponRawData1 = CouponRawData.builder()
                .faceValue(10.00)
                .limitValue(100.00)
                .type(1)
                .redPriceAvg(51.07)
                .redPrice50(67.01)
                .build();
        CouponRawData couponRawData2 = CouponRawData.builder()
                .faceValue(20.00)
                .limitValue(200.00)
                .type(3)
                .redPriceAvg(41.56)
                .redPrice50(56.01)
                .build();
        couponRawDataList.add(couponRawData1);
        couponRawDataList.add(couponRawData2);
        List<String> resList = couponService.modelPredict(couponRawDataList);
        log.info("结果为:{}", resList);
        return resList.toString();
    }
}

