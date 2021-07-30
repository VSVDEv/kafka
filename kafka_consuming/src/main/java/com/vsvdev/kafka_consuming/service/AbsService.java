package com.vsvdev.kafka_consuming.service;

import com.vsvdev.kafka_consuming.dto.Payment;

public interface AbsService {
  void transferPayment(Payment payment);
}
