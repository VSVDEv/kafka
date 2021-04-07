package com.vsvdev.spring_consumer;

import com.google.gson.JsonParser;
import org.apache.kafka.common.protocol.types.Field;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
@RestController
public class SpringConsumerApplication {



    List<String> messages = new ArrayList<>();

    User userFromTopic = null;

    @GetMapping("/consumeStringMessage")
    public List<String> consumeMsg() {
        return messages;
    }

    @GetMapping("/consumeJsonMessage")
    public User consumeJsonMessage() {
        return userFromTopic;
    }

    @KafkaListener(groupId = "vsvdev_cloud_app", topics = "vsvdev", containerFactory = "kafkaListenerContainerFactory")
    public List<String> getMsgFromTopic(String data) {
        messages.add(data);
        return messages;
    }

    @KafkaListener(groupId = "vsvdev_cloud_app-2", topics = "vsvdev", containerFactory = "userKafkaListenerContainerFactory")
    public User getJsonMsgFromTopic(User user) {
        userFromTopic = user;
        return userFromTopic;
    }
    public static void main(String[] args) {
        SpringApplication.run(SpringConsumerApplication.class, args);
    }

}
