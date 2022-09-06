package com.example.demo2.config;

import com.alibaba.fastjson.JSON;
import com.example.demo2.utils.ZipUtil;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import com.example.demo2.enity.*;
import org.springframework.web.client.RestTemplate;

@Component
@Data
public class weather {
    private String host="https://devapi.qweather.com/";
    private String nowpath="v7/weather/now?";
    private String key="key=bb7549a7dcc3407e890708b6588c7293";
    private String location="location=101200908";
    public JsonRootBean getweather() {
        String url = host + nowpath + location + "&" + key;
        RestTemplate restTemplate = new RestTemplate();
        byte[] zipbody = restTemplate.exchange(url, HttpMethod.GET, null, byte[].class).getBody();
        String body = ZipUtil.unGZip(zipbody);
        JsonRootBean entity = JSON.parseObject(body, JsonRootBean.class);
        System.out.println(entity.getNow().getText());
        return entity;
    }
}
