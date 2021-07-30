package com.vsvdev.kafka_consuming.exception;

public class ConsumerMisconfigurationException extends RuntimeException {
  public ConsumerMisconfigurationException(String msg) {
    super(msg);
  }
}
