package com.cc.model.impl;

import com.cc.domain.CouponRawData;
import com.cc.model.ModelPredictionTemplate;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author caichengjie
 * @date 2022/3/29
 */
@Data
@Slf4j
@Component("couponModel")
public class CouponModel extends ModelPredictionTemplate<CouponRawData> {
    @Value("${model.coupon}")
    private String modelPath;

    @Override
    protected String getModelPath() {
        return this.modelPath;
    }

    @Override
    protected Map<String, ?> featureEngineering(CouponRawData couponRawData) {
        Map<String, Object> featureMap = new HashMap<>();
        featureMap.put("face_log", couponRawData.getFaceValue());
        featureMap.put("limit_log", couponRawData.getLimitValue());
        featureMap.put("discount", couponRawData.getFaceValue() / couponRawData.getLimitValue());
        featureMap.put("is_cate", 1);
        featureMap.put("is_shop", 0);
        featureMap.put("is_sku", 0);
        featureMap.put("red_price_avg_log", couponRawData.getRedPriceAvg());
        featureMap.put("red_price_50_log", couponRawData.getRedPrice50());
        featureMap.put("limit_avg_ratio", couponRawData.getLimitValue() / couponRawData.getRedPriceAvg());
        featureMap.put("face_avg_ratio", couponRawData.getFaceValue() / couponRawData.getRedPriceAvg());
        featureMap.put("limit_50_ratio", couponRawData.getLimitValue() / couponRawData.getRedPrice50());
        featureMap.put("face_50_ratio", couponRawData.getFaceValue() / couponRawData.getRedPrice50());
        return featureMap;
    }
}
