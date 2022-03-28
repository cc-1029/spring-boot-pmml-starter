package com.cc.service.impl;

import com.cc.domain.TestRawData;
import com.cc.model.impl.TestModel;
import com.cc.service.TestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * Test 服务实现类
 *
 * @author caichengjie
 * @date 2022/3/28
 */
@Slf4j
@Service("testService")
public class TestServiceImpl implements TestService {

    @Autowired
    @Qualifier("testModel")
    private TestModel testModel;

    @Override
    public String test() {
        return "test Service";
    }

    @Override
    public String modelPredict(TestRawData testRawData) {
        Object predict = testModel.predict(testRawData);
        log.info("结果为 {}", predict);
        return predict.toString();
    }
}
