package com.hybris.guestbook;

import cucumber.api.java.Before;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Map;

public class SpringWirer {

    private static ClassPathXmlApplicationContext context;

    private static final String CONFIG = "classpath*:spring/*-config.xml";


    public static void wireSpring(){
        if (context == null) {
            context = new ClassPathXmlApplicationContext(CONFIG);
        }
    }

    public static ApplicationContext getContext() {
        return context;
    }

    public static void destroyContext(){
        if (context != null) {
            context.destroy();
            context.close();
            context = null;
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T getBean(final String name) {
        return (T) context.getBean(name);
    }

    public static <T> T getBean(Class<T> type) {
        return context.getBean(type);
    }

    public static <T> Map<String, T> getBeansOfType(Class<T> type) {
        return context.getBeansOfType(type);
    }

}
