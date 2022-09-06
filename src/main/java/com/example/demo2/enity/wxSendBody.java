package com.example.demo2.enity;

import lombok.Data;

import java.io.Serializable;
import com.example.demo2.enity.body.*;
@Data
public class wxSendBody implements Serializable {
    private String touser;
    private String template_id;
    private String appid="wx511f5a1a6debe57b";
    private com.example.demo2.enity.body.Data data;
}
