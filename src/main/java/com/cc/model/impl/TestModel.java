package com.cc.model.impl;

import com.cc.domain.TestRawData;
import com.cc.model.ModelPredictionTemplate;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.jpmml.evaluator.Evaluator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
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

    @Value("${model.test}")
    private String modelPath;

    private Evaluator model;

    /**
     * 预加载模型
     *
     * @param
     * @return
     */
    @PostConstruct
    private void initModel() {
        model = super.loadModelPyPath(modelPath);
        log.info("testModel 模型加载完成");
    }

    @Override
    protected Evaluator getModel() {
        return this.model;
    }

    @Override
    protected Map<String, Object> featureEngineering(TestRawData testRawData) {
        Map<String, Object> featureMap = new HashMap<>();
        featureMap.put("x1", testRawData.getX1());
        featureMap.put("x2", testRawData.getX2());
        return featureMap;
    }

    @Override
    protected Object transformOutput(TestRawData input, Object object) {
        return object;
    }

}
