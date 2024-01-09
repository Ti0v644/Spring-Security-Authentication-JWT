package com.mazen.seucrity.demo;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/demo-controller")
public class DemoController {
    @GetMapping
    public ResponseEntity<String> sayHello(){
        return ResponseEntity.ok("Hello from secured endpoint");
    }


    //eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhaG1lZEBnbWFpbC5jb20iLCJpYXQiOjE3MDQ3NTMzMTMsImV4cCI6MTcwNDc1NDc1M30.UmFa1ZAOUjz4kCJ79a9QZLsOr5OJEUCKN-1Jnl2Z3RA
    //eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhaG1lZEBnbWFpbC5jb20iLCJpYXQiOjE3MDQ3NTMyNzMsImV4cCI6MTcwNDc1NDcxM30.-qN2oeO8pcrz1iPUTZZGpr6rwVmJPSt44Bom_5j2ZMQ
    //eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhaG1lZEBnbWFpbC5jb20iLCJpYXQiOjE3MDQ3NTMxNDcsImV4cCI6MTcwNDc1NDU4N30.PlRwIUkn2uVP8UJWZGiqSVGL9mDkvD7u_jUBuHceOVM
}
