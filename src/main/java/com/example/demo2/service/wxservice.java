package com.example.demo2.service;

import com.alibaba.fastjson.JSON;
import com.example.demo2.config.weather;
import com.example.demo2.constant.wxtemplateConstant;
import com.example.demo2.enity.*;
import com.example.demo2.enity.body.*;
import com.example.demo2.utils.HttpUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class wxservice {
    @Autowired
    weather config;
    @Scheduled(cron = "0 0,30 0,5 ? * ? ")
    public void sentTemplate() throws Exception {
        token token=gettoken();
        System.out.println(token.getAccess_token());
        String openid="o1D5F6qHay5qyC3Te-7ozMNVmZnc";
        String accessToken= token.getAccess_token();
        String host=wxtemplateConstant.WECHAT_HOST;
        String path=wxtemplateConstant.SEND_TEMPLATE_PATH+accessToken;
        JsonRootBean weather=config.getweather();
        String translation=wxtemplateConstant.TRANSLATION_VALUE;
        String text=weather.getNow().getText();
        String temp=weather.getNow().getTemp();
        String feelslike=weather.getNow().getFeelsLike();
        wxSendBody body1=getbody(openid,wxtemplateConstant.TEMPLATE_SEND_FIRST,text,temp,feelslike,translation);
        String body=JSON.toJSONString(body1);
        System.out.println(body);
        HttpResponse postresponse=HttpUtils.doPost(host,path,"post",new HashMap<>(),new HashMap<>(),body);
        HttpEntity entity=postresponse.getEntity();
        String json=EntityUtils.toString(entity);
        System.out.println(json);
    }
    public token gettoken() throws Exception {
        String path= wxtemplateConstant.GET_WECHAT_TOKEN_PATH;
        System.out.println(path);
        HttpResponse response= HttpUtils.doGet(wxtemplateConstant.WECHAT_HOST,path,"GET",new HashMap<>(),new HashMap<>());
        HttpEntity responseEnity= response.getEntity();
        String json= EntityUtils.toString(responseEnity);
        token token= JSON.parseObject(json,token.class);
        return token;
    }
    public follower getFollower() throws Exception {
        token token=gettoken();
        String accesstoken=token.getAccess_token();
        String host=wxtemplateConstant.WECHAT_HOST;
        String path=wxtemplateConstant.GET_FOLLOWER_TOKEN_PATH+accesstoken;
        HttpResponse response=HttpUtils.doGet(host,path,"get",new HashMap<>(),new HashMap<>());
        HttpEntity responsEntity=response.getEntity();
        String json=EntityUtils.toString(responsEntity);
        follower follower=JSON.parseObject(json,follower.class);
        return follower;
    }
    private wxSendBody getbody(String openid,String id,String text,String temp,String feelslike,String translation) throws Exception {
        wxSendBody body=new wxSendBody();
        java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar=Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
        Date date = calendar.getTime();
        String week = sdf.format(date);
        body.setTouser(openid);
        body.setTemplate_id(id);
        Data data=new Data();
        Text text1=new Text(text,wxtemplateConstant.COLOR_YELLOW);
        Temp temp1=new Temp(temp,wxtemplateConstant.COLOR_BLUE);
        Feelslike feelslike1=new Feelslike(feelslike,wxtemplateConstant.COLOR_ORANGE);
        Translate translate1=new Translate(translation,wxtemplateConstant.COLOR_BLUE);
        Loveday loveday1=new Loveday(getLoveday(),wxtemplateConstant.COLOR_PINK);
        birthday birthday=new birthday(getBirthday(),wxtemplateConstant.COLOR_ORANGE);
        Time time=new Time(format.format(calendar.getTime())+week,wxtemplateConstant.COLOR_GREEN);
        noteEntity noteEntity=getNote();
        Note note=new Note(noteEntity.getHitokoto(),wxtemplateConstant.COLOR_RED);
        from from=new from(noteEntity.getFrom(),wxtemplateConstant.COLOR_RED);
        data.setFeelslike(feelslike1);
        data.setTemp(temp1);
        data.setText(text1);
        data.setTranslate(translate1);
        data.setLoveday(loveday1);
        data.setBirthday(birthday);
        data.setTime(time);
        data.setNote(note);
        data.setFrom(from);
        body.setData(data);
        return body;
    }
    public String getLoveday() throws ParseException {
        java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar=Calendar.getInstance();
        Date date = calendar.getTime();
        System.out.println(format.format(calendar.getTime()));
        java.util.Date beginDate= format.parse("2022-08-22");
        java.util.Date endDate= format.parse(format.format(calendar.getTime()));
        long day=(endDate.getTime()-beginDate.getTime())/(24*60*60*1000);
        System.out.println(day);
        return "今天是我们相恋第 "+String.valueOf(day)+" 天";
    }
    public String getBirthday() throws ParseException {
        java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar=Calendar.getInstance();
        Date date = calendar.getTime();
        System.out.println(format.format(calendar.getTime()));
        java.util.Date endDate= format.parse("2022-11-22");
        java.util.Date beginDate= format.parse(format.format(calendar.getTime()));
        long day=(endDate.getTime()-beginDate.getTime())/(24*60*60*1000);
        System.out.println(day);
        return "距离宝贝生日还有 "+String.valueOf(day)+" 天";
    }
    public noteEntity getNote() throws Exception {
        String host="https://v1.hitokoto.cn";
        String path="/?c=d";
        HttpResponse response=HttpUtils.doGet(host,path,"get",new HashMap<>(),new HashMap<>());
        HttpEntity responsEntity=response.getEntity();
        String json=EntityUtils.toString(responsEntity);
        noteEntity note=JSON.parseObject(json,noteEntity.class);
        return note;
    }
}
