package com.vsvdev.kafka_consuming.exception;


import java.util.UUID;

/**
 * Thrown in case of ABS is unavailable at the moment for sync processing.
 */
public class AbsUnavailableException extends RuntimeException {
  public AbsUnavailableException(UUID idempotencyKey) {
    super("Payment idempotency key = " + idempotencyKey);
  }
}
