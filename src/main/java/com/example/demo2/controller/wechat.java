package com.example.demo2.controller;

import com.example.demo2.service.wxservice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

@Controller
public class wechat {
    @Autowired
    wxservice wxservice;
    @RequestMapping("/sendmsg")
    public String sendmsg() throws Exception {
        wxservice.sentTemplate();
//        Calendar calendar=Calendar.getInstance();
//        calendar.set(Calendar.HOUR_OF_DAY,8);
//        calendar.set(Calendar.MINUTE,43);
//        calendar.set(Calendar.SECOND,0);
//        Date time=calendar.getTime();
//        Timer timer=new Timer();
//        TimerTask timerTask=new TimerTask() {
//            @Override
//            public void run() {
//                try {
//                    wxservice.sentTemplate();
//                }
//                catch (Exception e){
//                    e.printStackTrace();
//                }
//            }
//        };
//        timer.schedule(timerTask,time,1000*60*60*24);
        return "success";
    }
}
