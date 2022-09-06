package com.example.demo2;

import com.example.demo2.service.wxservice;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.ParseException;

@SpringBootTest
class Demo2ApplicationTests {
    @Autowired
    wxservice wxservice;
    @Test
    void contextLoads() throws Exception {
        System.out.println(wxservice.getNote().getHitokoto());
    }

}
