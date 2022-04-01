package com.cc.service.impl;

import com.cc.domain.TestRawData;
import com.cc.model.impl.TestModel;
import com.cc.service.TestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
    public List<String> modelPredict(TestRawData testRawData) {
        List<TestRawData> testRawDataList = new ArrayList<>();
        testRawDataList.add(testRawData);
        List<Object> objectList = testModel.predict(testRawDataList);
        log.info("结果为 {}", objectList);
        return objectList.stream().map(Object::toString).collect(Collectors.toList());
    }
}
