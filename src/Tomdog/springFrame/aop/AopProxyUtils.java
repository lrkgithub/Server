package Tomdog.springFrame.aop;

import java.lang.reflect.Field;
import java.lang.reflect.Proxy;

public class AopProxyUtils {

    public static Object getTargetObject(Object proxy) throws NoSuchFieldException, IllegalAccessException {

        if (!isAopProxy(proxy)) {
            return proxy;
        }

        return getProxyTargetObject(proxy);

    }

    private static boolean isAopProxy(Object object) {

        return Proxy.isProxyClass(object.getClass());

    }

    private static Object getProxyTargetObject(Object proxy) throws NoSuchFieldException, IllegalAccessException {

        Field h = proxy.getClass().getSuperclass().getDeclaredField("h");
        h.setAccessible(true);
        AopProxy aopProxy = (AopProxy)h.get(proxy);
        Field target = aopProxy.getClass().getDeclaredField("target");
        target.setAccessible(true);
        return target.get(aopProxy);
    }

}
