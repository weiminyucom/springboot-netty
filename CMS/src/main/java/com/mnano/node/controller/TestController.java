package com.mnano.node.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author weimin
 * @ClassName TestController
 * @Description TODO
 * @date 2021/1/8 18:29
 */
@RestController
public class TestController {

    @RequestMapping(value = "/test")
    public Object test(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Map<String,Object> map = new HashMap<>();
        map.put("xxxx","dklsjkjs");
        return map;
    }
}
