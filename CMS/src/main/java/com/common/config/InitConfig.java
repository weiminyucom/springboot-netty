package com.common.config;

import com.common.util.SpringUtils;
import org.springframework.beans.BeansException;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * @author weimin
 * @ClassName InitConfig
 * @Description TODO
 * @date 2020/12/29 9:31
 */
@Component
public class InitConfig implements CommandLineRunner, ApplicationContextAware {


    @Override
    public void run(String... args) throws Exception {
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringUtils.setApplicationContext(applicationContext);
    }
}
