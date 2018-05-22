package Tomdog.springFrame.aop;


import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

//配置增强类
//对application中的expression的封装
public class AopConfig {

//    以目标对象需要增强的Method作为key，需要增强的代码内容作为value
    private Map<Method, Aspect> points = new HashMap<Method, Aspect>();

    public void put(Method method, Object aspect, Method[] points) {
        this.points.put(method, new Aspect(aspect, points));
    }

    public Aspect get(Method method) {
        return this.points.get(method);
    }

    public boolean contains(Method method) {
        return this.points.containsKey(method);
    }

}
