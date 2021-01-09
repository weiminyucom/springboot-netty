package com;

import com.common.netty.server.NettySocketServer;
import com.common.util.SpringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.mock.web.MockServletConfig;
import org.springframework.web.servlet.DispatcherServlet;

import java.lang.reflect.Field;

/**
 * @author m
 */
@SpringBootApplication()
@Slf4j
public class CmsApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(CmsApplication.class, args);
        DispatcherServlet dispatcherServlet = run.getBean(DispatcherServlet.class);
        MockServletConfig myServletConfig = new MockServletConfig();
        setFieldValue(dispatcherServlet,"config",myServletConfig);
        try {
            dispatcherServlet.init();
        } catch (Exception e) {
            log.error("e:",e);
        }
        NettySocketServer bean = SpringUtils.getBean(NettySocketServer.class);
        bean.nettyStart();
    }

    public static void setFieldValue(Object object, String fieldName, Object value){

        //根据 对象和属性名通过反射 调用上面的方法获取 Field对象
        Field field = getDeclaredField(object, fieldName) ;

        //抑制Java对其的检查
        field.setAccessible(true) ;

        try {
            //将 object 中 field 所代表的值 设置为 value
            field.set(object, value) ;
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }

    public static Field getDeclaredField(Object object, String fieldName){
        Field field = null ;

        Class<?> clazz = object.getClass() ;

        for(; clazz != Object.class ; clazz = clazz.getSuperclass()) {
            try {
                field = clazz.getDeclaredField(fieldName) ;
                return field ;
            } catch (Exception e) {

            }
        }

        return null;
    }
}
