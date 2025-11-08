package com.cjlabs.boot.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class FmkBootController {

    @PostMapping("/api/fmk/boot/health")
    public String health() {
        return "OK";
    }

    @PostMapping("/api/fmk/boot/info")
    public Map<String, Object> info() {
        Map<String, Object> info = new HashMap<>();
        info.put("framework", "fmk-boot");
        info.put("version", "20250924_v1");
        return info;
    }
}