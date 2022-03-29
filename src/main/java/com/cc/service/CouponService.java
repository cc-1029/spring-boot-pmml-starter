package com.cc.service;

import com.cc.domain.CouponRawData;
import com.cc.domain.TestRawData;

import java.util.List;

/**
 * @author caichengjie
 * @date 2022/3/29
 */
public interface CouponService {
    /**
     * test
     *
     * @return String
     */
    List<String> modelPredict(List<CouponRawData> couponRawData);
}
