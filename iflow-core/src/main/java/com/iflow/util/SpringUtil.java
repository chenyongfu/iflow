package com.iflow.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * spring工具类
 */
public class SpringUtil implements ApplicationContextAware {

    private static Logger logger = LoggerFactory.getLogger(SpringUtil.class);

    private static ApplicationContext context = null;



    /**
     * Set the ApplicationContext that this object runs in.
     * Normally this call will be used to initialize the object.
     * <p>Invoked after population of normal bean properties but before an init callback such
     * as {@link InitializingBean#afterPropertiesSet()}
     *
     * @param applicationContext the ApplicationContext object to be used by this object
     * @throws BeansException              if thrown by application context methods
     * @see BeanInitializationException
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
        logger.info("ApplicationContextAware初始化完成");
    }


    /**
     * 获取bean
     * @param beanId
     * @return
     */
    public synchronized static Object getBean(String beanId) {
        return context.getBean(beanId);
    }


    /**
     * 获取spring环境
     * @return
     */
    public synchronized static ApplicationContext getApplicationContext() {
        return context;
    }

}