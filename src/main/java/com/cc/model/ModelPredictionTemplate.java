package com.cc.model;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.dmg.pmml.FieldName;
import org.jpmml.evaluator.*;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.util.CollectionUtils;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 模板方法设计模式：模型预测的模板类
 *
 * @author caichengjie
 * @date 2022/3/28
 */
@Data
@Slf4j
public abstract class ModelPredictionTemplate<Input, Output> {

    /**
     * 模板方法：基于模型和数据进行预测
     *
     * @param inputList
     * @return outputList
     */
    public final List<Output> predict(List<Input> inputList) {
        // 如果模型入参为空，则直接返回null
        if (CollectionUtils.isEmpty(inputList)) {
            return null;
        }
        // 第一步：加载模型
        Evaluator model = getModel();
        // 如果模型为空，则直接返回null
        if (model == null || CollectionUtils.isEmpty(model.getInputFields())) {
            return null;
        }
        // 第二步：将原始数据进行特征工程、加载数据、根据模型预测结果
        List<Output> outputList = new ArrayList<>();
        for (Input input: inputList) {
            Object o = featureEngineeringAndPredict(model, input);
            Output output = transformOutput(input, o);
            outputList.add(output);
        }
        return outputList;
    }

    /**
     * 根据模型路径加载模型
     *
     * @param modelPath
     * @return model
     */
    protected Evaluator loadModelPyPath(String modelPath) {
        log.info("loadModelPyPath {}", modelPath);
        InputStream inputStream = null;
        try {
            // SpringBoot项目通过这种方式获得Resources文件夹下的文件
            Resource resource = new ClassPathResource(modelPath);
            inputStream = resource.getInputStream();
            Evaluator model = new LoadingModelEvaluatorBuilder()
                    .load(inputStream)
                    .build();
            model.verify();
            log.info("模型加载完毕");
            return model;
        } catch (IOException | JAXBException | SAXException e) {
            log.error("根据模型路径加载机器学习模型失败,模型路径为:{}", modelPath);
            return null;
        } finally {
            try {
                assert inputStream != null;
                inputStream.close();
            } catch (IOException e) {
                log.error("InputStream关闭失败");
                e.printStackTrace();
            }
        }
    }

    /**
     * 转换特征
     *
     * @param model, featureMap
     * @return res
     */
    private Map<FieldName, FieldValue> transformFeature(Evaluator model, Map<String, ?> featureMap) {
        // 根据模型获得输入的域
        List<InputField> inputFieldList = model.getInputFields();
        Map<FieldName, FieldValue> arguments = new LinkedHashMap<>();
        for (InputField inputField : inputFieldList) {
            FieldName fieldName = inputField.getName();
            Object rawValue = featureMap.get(fieldName.getValue());
            FieldValue fieldValue = inputField.prepare(rawValue);
            arguments.put(fieldName, fieldValue);
        }
        return arguments;
    }

    /**
     * 特征工程后并进行预测
     *
     * @param input
     * @return object
     */
    private Object featureEngineeringAndPredict(Evaluator model, Input input) {
        // 基于子类实现的特征工程方法进行转换
        Map<String, ?> featureMap = featureEngineering(input);
        if (featureMap == null || featureMap.size() == 0) {
            return null;
        }
        // 根据特征加载数据 确定输入
        Map<FieldName, FieldValue> arguments = transformFeature(model, featureMap);
        // 评估模型，得到结果
        Map<FieldName, ?> results = model.evaluate(arguments);
        // 默认先处理单输出的模型 TODO 后续考虑加入多输出的处理
        List<TargetField> targetFields = model.getTargetFields();
        FieldName targetFieldName = targetFields.get(0).getName();
        Object targetFieldValue = results.get(targetFieldName);
        if (isNumber(targetFieldValue)) {
            return targetFieldValue;
        }
        if (isComputable(targetFieldValue)) {
            return ((Computable) targetFieldValue).getResult();
        }
        return null;
    }

    /**
     * 判断是不是数字
     *
     * @return o
     */
    private Boolean isNumber(Object o) {
        return o instanceof Number;
    }

    /**
     * 判断是不是数字
     *
     * @return o
     */
    private Boolean isComputable(Object o) {
        return o instanceof Computable;
    }

    /**
     * 返回模型路径，不同模型返回不同路径
     *
     * @return modelPath
     */
    protected abstract Evaluator getModel();

    /**
     * 特征工程，根据原始数据返回特征
     *
     * @param input
     * @return feature
     */
    protected abstract Map<String, ?> featureEngineering(Input input);

    /**
     * 改变输出格式
     *
     * @param object
     * @return output
     */
    protected abstract Output transformOutput(Input input, Object object);

}
