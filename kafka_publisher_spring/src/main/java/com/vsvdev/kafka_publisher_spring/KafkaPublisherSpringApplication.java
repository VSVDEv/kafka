package com.vsvdev.kafka_publisher_spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class KafkaPublisherSpringApplication {

    private KafkaTemplate<String, Object> template;

    @Autowired
    public void setTemplate(KafkaTemplate<String, Object> template) {
        this.template = template;
    }

    private String topic = "vsvdev";

    @GetMapping("/publish/{name}")
    public String publishMessage(@PathVariable String name) {
        template.send(topic, "Hi " + name + " Welcome to VSVDev");
        return "Data published";
    }

    @GetMapping("/publishJson")
    public String publishMessage() {
        User user = new User(2532, "User77", new String[] { "Kiev", "obolon", "house 90" });
        template.send(topic, user);
        return "Json Data published";
    }

    public static void main(String[] args) {
        SpringApplication.run(KafkaPublisherSpringApplication.class, args);
    }
}
