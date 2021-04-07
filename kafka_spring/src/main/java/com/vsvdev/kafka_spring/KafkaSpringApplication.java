package com.vsvdev.kafka_spring;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.flogger.FluentLogger;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.Acknowledgment;

import java.util.List;

@SpringBootApplication
public class KafkaSpringApplication {
    private static final FluentLogger log = FluentLogger.forEnclosingClass();

    private final KafkaTemplate kafkaTemplate;

    private ObjectMapper objectMapper = new ObjectMapper().configure(
            DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    @Autowired
    public KafkaSpringApplication(KafkaTemplate kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public static void main(String[] args) {
        SpringApplication.run(KafkaSpringApplication.class, args);
    }

    @KafkaListener(topics = {"important_tweets"}, containerFactory = "myContainerFactory")
    public void listen(List<ConsumerRecord<Long, Tweet>> records, Acknowledgment acknowledgment) {
        log.atFine().log("reveived %d tweets", records.size());
        for (ConsumerRecord<Long, Tweet> record: records) {
            log.atFine().log("received tweet %d in partition %d", record.key(), record.partition());
        }
        acknowledgment.acknowledge();
    }


}
