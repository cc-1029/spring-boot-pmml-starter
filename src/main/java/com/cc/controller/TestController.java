package com.cc.controller;

import com.cc.domain.TestRawData;
import com.cc.service.TestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 测试 Controller
 *
 * @author caichengjie
 * @date 2022/3/28
 */
@Slf4j
@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    @Qualifier("testService")
    private TestService testService;


    @RequestMapping("/hello")
    public String hello() {
        return testService.test();
    }

    @RequestMapping("/model")
    public String model() {
        TestRawData testRawData = new TestRawData();
        Double res = Double.parseDouble(testService.modelPredict(testRawData));
        return res.toString();
    }
}
