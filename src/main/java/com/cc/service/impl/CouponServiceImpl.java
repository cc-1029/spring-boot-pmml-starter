package com.cc.service.impl;

import com.cc.domain.CouponRawData;
import com.cc.domain.TestRawData;
import com.cc.model.impl.CouponModel;
import com.cc.model.impl.TestModel;
import com.cc.service.CouponService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author caichengjie
 * @date 2022/3/29
 */
@Slf4j
@Service("couponService")
public class CouponServiceImpl implements CouponService {

    @Autowired
    @Qualifier("couponModel")
    private CouponModel couponModel;

    @Override
    public List<String> modelPredict(List<CouponRawData> couponRawData) {
        List<Object> objectList = couponModel.predict(couponRawData);
        log.info("结果为 {}", objectList);
        return objectList.stream().map(Object::toString).collect(Collectors.toList());
    }
}
