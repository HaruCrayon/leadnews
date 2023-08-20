package com.heima.kafka.controller;

import com.alibaba.fastjson.JSON;
import com.heima.kafka.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author LiJing
 * @version 1.0
 */
@RestController
public class HelloController {
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @GetMapping("/hello")
    public String hello() {
//        kafkaTemplate.send("topic-second", "你好 spring-kafka");

        User user = new User();
        user.setUserName("小王");
        user.setAge(18);

        kafkaTemplate.send("topic-user", JSON.toJSONString(user));

        return "ok";
    }
}
