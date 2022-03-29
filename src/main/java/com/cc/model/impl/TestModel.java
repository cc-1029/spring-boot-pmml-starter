package com.cc.model.impl;

import com.cc.domain.TestRawData;
import com.cc.model.ModelPredictionTemplate;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author caichengjie
 * @date 2022/3/28
 */
@Data
@Slf4j
@Component("testModel")
public class TestModel extends ModelPredictionTemplate<TestRawData, Object> {

//    @Value("${model.test}")
    private String modelPath;

    @Override
    protected String getModelPath() {
        return this.modelPath;
    }

    @Override
    protected Map<String, Object> featureEngineering(TestRawData testRawData) {
        Map<String, Object> featureMap = new HashMap<>();
        return featureMap;
    }

    @Override
    protected Object transformOutput(Object object) {
        return object;
    }

}
