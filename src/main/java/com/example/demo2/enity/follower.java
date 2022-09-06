package com.example.demo2.enity;

import lombok.Data;

import java.util.List;

@Data
public class follower {
    private int total;
    private int count;
    private Data data;
    private String next_openid;
    @lombok.Data
    public class Data{
        private List<String> openid;
    }
}
