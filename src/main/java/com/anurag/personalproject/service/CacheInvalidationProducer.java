package com.anurag.personalproject.service;

import com.anurag.personalproject.event.CacheInvalidationEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import java.time.Instant;

@Service
@RequiredArgsConstructor
@Slf4j
public class CacheInvalidationProducer {

    private final KafkaTemplate<String, CacheInvalidationEvent> kafkaTemplate;

    private static final String TOPIC = "product-cache-invalidation";

    public void publishInvalidation(Long productId, String eventType) {
        CacheInvalidationEvent event = new CacheInvalidationEvent(
                productId,
                eventType,
                Instant.now()
        );
        kafkaTemplate.send(TOPIC, productId.toString(), event);
        log.info("Published cache invalidation event for productId: {} eventType: {}",
                productId, eventType);
    }
}