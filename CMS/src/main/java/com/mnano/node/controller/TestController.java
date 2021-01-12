package com.mnano.node.controller;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.io.IOException;
import java.util.Map;

/**
 * @author weimin
 * @ClassName TestController
 * @Description TODO
 * @date 2021/1/8 18:29
 */
@RestController
@RequestMapping("api/v1")
public class TestController {

    @RequestMapping(value = "/registeredNode")
    public Object test(@RequestBody Map<String,Object> map) throws IOException {
        System.out.println(map);
        map.put("xxxx","dklsjkjs");
        return map;
    }
}
