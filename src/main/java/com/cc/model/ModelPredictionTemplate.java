package com.cc.model;

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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 模板方法设计模式：模型预测的模板类
 *
 * @author caichengjie
 * @date 2022/3/28
 */
@Slf4j
public abstract class ModelPredictionTemplate<RawData> {

    /**
     * 模板方法：基于模型和数据进行预测
     *
     * @param rawData
     */
    public Object predict(RawData rawData) {
        // 第一步：根据模型路径加载模型
        String modelPath = getModelPath();
        Evaluator model = loadModelPyPath(modelPath);
        // 如果模型为空或模型入参为空，则直接返回null
        if (model == null || CollectionUtils.isEmpty(model.getInputFields())) {
            return null;
        }
        // 第二步：特征工程，将原始数据转换为特征
        Map<String, Object> featureMap = featureEngineering(rawData);
        if (featureMap == null || featureMap.size() == 0) {
            return null;
        }
        // 第三步：评估模型得到结果
        // 根据特征加载数据 确定输入
        Map<FieldName, FieldValue> arguments = transformFeature(model, featureMap);
        // 评估模型，得到结果
        Map<FieldName, ?> results = model.evaluate(arguments);
        List<TargetField> targetFields = model.getTargetFields();
        FieldName targetFieldName = targetFields.get(0).getName();
        Object targetFieldValue = results.get(targetFieldName);
        if (targetFieldValue instanceof Computable) {
            return ((Computable) targetFieldValue).getResult();
        }
        return null;
    }

    /**
     * 根据模型路径加载模型
     *
     * @param modelPath
     * @return model
     */
    private Evaluator loadModelPyPath(String modelPath) {
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
     * @param model
     * @return model
     */
    private Map<FieldName, FieldValue> transformFeature(Evaluator model, Map<String, Object> featureMap) {
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
     * 返回模型路径，不同模型返回不同路径
     *
     * @return modelPath
     */
    protected abstract String getModelPath();

    /**
     * 特征工程，根据原始数据返回特征
     *
     * @param rawData
     * @return feature
     */
    protected abstract Map<String, Object> featureEngineering(RawData rawData);

}
