package com.anurag.personalproject.service;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class CacheStatService {
    private final AtomicLong totalRequest = new AtomicLong(0);
    private final AtomicLong requestMiss = new AtomicLong(0);
    public void recordMiss(){
        requestMiss.incrementAndGet();
    }
    public void recordRequest(){
        totalRequest.incrementAndGet();
    }
    public Map<String, Object> getStats(){
        long total = totalRequest.get();
        long miss = requestMiss.get();
        long hits = total-miss;
        double hitRate = total == 0 ? 0 : (((double)hits/total)*100);
        return Map.of("totalRequests", total, "cacheHits", hits, "cacheMisses", miss, "hitRate", String.format("%.1f%%",  hitRate));
    }
}
