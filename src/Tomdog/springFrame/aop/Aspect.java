package Tomdog.springFrame.aop;

import java.lang.reflect.Method;

public class Aspect {

    private Object aspect;

    private Method[] points;

    public Aspect(Object aspect, Method[] points) {
        this.aspect = aspect;
        this.points = points;
    }

    public Object getAspect() {
        return aspect;
    }

    public void setAspect(Object aspect) {
        this.aspect = aspect;
    }

    public Method[] getPoints() {
        return points;
    }

    public void setPoints(Method[] points) {
        this.points = points;
    }
}
