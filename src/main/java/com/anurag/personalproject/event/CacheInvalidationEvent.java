package com.anurag.personalproject.event;

import java.time.Instant;

public record CacheInvalidationEvent(
        Long productId,
        String eventType,
        Instant timestamp
) {}
