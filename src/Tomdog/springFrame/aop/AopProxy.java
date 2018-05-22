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

//        原始方法调用前，需要执行增强的代码
//        需要通过原生方法去找，通过代理方法在map中是找不到的
        if (config.contains(method)) {
            Aspect aspect = config.get(method);
            aspect.getPoints()[0].invoke(aspect);
        }

//        原始方法调用
        Object obj = method.invoke(this.target, args);

        if (config.contains(method)) {
            Aspect aspect = config.get(method);
            aspect.getPoints()[1].invoke(aspect);
        }

        return obj;
    }

    public void setConfig(AopConfig aopConfig) {
        this.config = aopConfig;
    }
}
