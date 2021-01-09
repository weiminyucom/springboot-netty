package com.common.util;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * @author weimin
 */
@Slf4j
public class SpringUtils {

    private static ApplicationContext applicationContext = null;

    public static void setApplicationContext(ApplicationContext applicationContext){
        if(SpringUtils.applicationContext == null){
            log.info("set applicationContext");
            SpringUtils.applicationContext  = applicationContext;
        }

    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public static Object getBean(String name){
        return getApplicationContext().getBean(name);

    }

    public static <T> T getBean(Class<T> clazz){
        return getApplicationContext().getBean(clazz);
    }

    public static <T> T getBean(String name,Class<T> clazz){
        return getApplicationContext().getBean(name, clazz);
    }

    public static void stopSpring(){
        if(applicationContext instanceof ConfigurableApplicationContext){
            ConfigurableApplicationContext context = (ConfigurableApplicationContext) applicationContext;
            context.close();
        }
    }

    public static String getProperty(String name){
        return applicationContext.getEnvironment().getProperty(name);
    }
}
