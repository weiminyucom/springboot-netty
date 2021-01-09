package com.common.listener;

import com.common.config.MyServletContext;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringApplicationRunListener;
import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.stereotype.Component;

import javax.servlet.ServletContext;


/**
 * @author weimin
 */
@Data
@Slf4j
public class SpringRunListener implements SpringApplicationRunListener {

    public SpringRunListener(SpringApplication application, String[] args) {
        super();
    }


    @Override
    public void starting() {}

    @Override
    public void environmentPrepared(ConfigurableEnvironment environment) {}

    @Override
    public void contextPrepared(ConfigurableApplicationContext context) {
        ServletContext servletContext = new MyServletContext();
        ServletWebServerApplicationContext applicationContext = (ServletWebServerApplicationContext) context;
        applicationContext.setServletContext(servletContext);
    }

    @Override
    public void contextLoaded(ConfigurableApplicationContext context) {}

    @Override
    public void started(ConfigurableApplicationContext context) { }

    @Override
    public void running(ConfigurableApplicationContext context) {}

    @Override
    public void failed(ConfigurableApplicationContext context, Throwable exception) {}


}
