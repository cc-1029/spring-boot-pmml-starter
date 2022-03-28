package com.cc.service;

import com.cc.domain.TestRawData;

/**
 * Test 服务
 *
 * @author caichengjie
 * @date 2022/3/28
 */
public interface TestService {

    /**
     * test
     *
     * @return String
     */
    String test();

    /**
     * test
     *
     * @return String
     */
    String modelPredict(TestRawData testRawData);
}
