package com.vsvdev.kafka_consuming.exception;

public class PaymentInvalidException extends RuntimeException {
  public PaymentInvalidException(String msg) {
    super(msg);
  }
}
