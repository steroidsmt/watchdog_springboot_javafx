package com.example.demo.service;

import org.springframework.stereotype.Service;

@Service
public class StartOrStop {
    public  String setStartButonText() {
        return "Start";
    }
    public  String setStopButonText() {
        return "Stop";
    }
}
