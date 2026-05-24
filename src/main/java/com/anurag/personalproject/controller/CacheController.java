package com.anurag.personalproject.controller;


import com.anurag.personalproject.service.CacheStatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("api")
@RequiredArgsConstructor
public class CacheController {
    private final CacheStatService cacheStatService;
    @GetMapping("cache/stats")
    public ResponseEntity<Map<String,Object>> getRedisStats() {
        return ResponseEntity.ok(cacheStatService.getStats());
    }

}
