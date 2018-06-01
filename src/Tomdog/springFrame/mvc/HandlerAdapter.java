package Tomdog.springFrame.mvc;

import Tomdog.http.HttpRequest;
import Tomdog.http.HttpResponse;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

public class HandlerAdapter {

    private Map<String, Integer> paramMapping;

    public HandlerAdapter(Map<String, Integer> paramMapping) {
        this.paramMapping = paramMapping;
    }

    public ModelAndView handle(HttpRequest httpRequest, HttpResponse httpResponse, HandlerMapping handlerMapping) throws InvocationTargetException, IllegalAccessException {

        Class<?>[] paramTypes = handlerMapping.getMethod().getParameterTypes();

        Map<String, String> reqParameterMap = httpRequest.getParameter();

        Object[] paramValues = new Object[paramTypes.length];

        for (Map.Entry<String, String> param : reqParameterMap.entrySet()) {

            String value = param.getValue().replaceAll("\\[|\\]", "").replaceAll("\\s", "");

            if (!this.paramMapping.containsKey(param.getKey())) {
                continue;
            }

            int index = this.paramMapping.get(value);

            paramValues[index] = caseStringValue(value, paramTypes[index]);
        }

        if (this.paramMapping.containsKey(HttpRequest.class.getName())) {
            int reqIndex = this.paramMapping.get(HttpRequest.class.getName());
            paramValues[reqIndex] = httpRequest;
        }

        if (this.paramMapping.containsKey(HttpResponse.class.getName())) {
            int rpnIndex = this.paramMapping.get(HttpResponse.class.getName());
            paramValues[rpnIndex] = httpResponse;
        }

        Object result = handlerMapping.getMethod().invoke(handlerMapping.getController(), paramValues);

        boolean isModelAndView = handlerMapping.getMethod().getReturnType() == ModelAndView.class;

        if (isModelAndView) {
            return (ModelAndView)result;
        } else {
            return null;
        }
    }

    private Object caseStringValue(String value, Class<?> type) {

        if (type == String.class) {
            return value;
        } else if (type == int.class || type == Integer.class) {
            return Integer.valueOf(value);
        } else {
            return null;
        }
    }
}
