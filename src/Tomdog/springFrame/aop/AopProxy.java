package Tomdog.springFrame.aop;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class AopProxy implements InvocationHandler {

    private AopConfig config;

    private Object target;

    public Object getProxy(Object instance) {
        this.target = instance;
        Class<?> clazz = instance.getClass();
        return Proxy.newProxyInstance(clazz.getClassLoader(),
                                        clazz.getInterfaces(),
                                        this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Method m = this.target.getClass().getMethod(method.getName(), method.getParameterTypes());


//        if (config.conAtains(method))
        return null;
    }

    public void setConfig(AopConfig aopConfig) {
        this.config = aopConfig;
    }
}
