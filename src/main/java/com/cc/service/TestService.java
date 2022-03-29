package com.cc.service;

import com.cc.domain.TestRawData;

import java.util.List;

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
    List<String> modelPredict(TestRawData testRawData);
}
