package Tomdog.springFrame.aop;

import java.lang.reflect.Proxy;

public class AopProxyUtils {

    public static Object getTargetObject(Object proxy) {

        if (!isAopProxy(proxy)) {
            return proxy;
        }

        return getProxyTargetObject(proxy);

    }

    private static boolean isAopProxy(Object object) {

        return Proxy.isProxyClass(object.getClass());

    }

    private static Object getProxyTargetObject(Object proxy) {
        return null;
    }

}
