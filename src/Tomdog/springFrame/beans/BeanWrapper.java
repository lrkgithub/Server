package Tomdog.springFrame.beans;

import Tomdog.springFrame.aop.AopConfig;
import Tomdog.springFrame.aop.AopProxy;
import Tomdog.springFrame.core.FactoryBean;

public class BeanWrapper extends FactoryBean {

    private BeanPostProcessor postProcessor;
    private AopProxy aopProxy;
    private Object wrapperInstance;
    private Object instance;

    public BeanWrapper(Object instance) {
        this.instance = instance;
        this.wrapperInstance = instance;
    }

    public Class<?> getCWrapperClass() {
        return this.wrapperInstance.getClass();
    }

    public void setAopConfig(AopConfig aopConfig) {
        this.aopProxy.setConfig(aopConfig);
    }

    public AopProxy getAopProxy() {
        return aopProxy;
    }

    public void setAopProxy(AopProxy aopProxy) {
        this.aopProxy = aopProxy;
    }

    public Object getInstance() {
        return instance;
    }

    public BeanPostProcessor getPostProcessor() {
        return postProcessor;
    }

    public void setPostProcessor(BeanPostProcessor postProcessor) {
        this.postProcessor = postProcessor;
    }

    public Object getWrapperInstance() {
        return wrapperInstance;
    }

    public void setWrapperInstance(Object wrapperInstance) {
        this.wrapperInstance = wrapperInstance;
    }

    public void setInstance(Object instance) {
        this.instance = instance;
    }
}
