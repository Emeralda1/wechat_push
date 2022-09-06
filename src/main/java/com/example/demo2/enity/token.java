package com.example.demo2.enity;

import lombok.Data;

@Data
public class token {
    // 接口访问凭证
    private String access_token;
    // 凭证有效期，单位：秒
    private int expires_in;
}
