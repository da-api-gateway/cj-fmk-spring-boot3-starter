package com.cjlabs.boot.controller;

import com.cjlabs.web.anno.NoLogin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@NoLogin
@RestController
public class FmkBootController {

    @PostMapping("/fmk/health")
    public String health() {
        return "OK";
    }

}