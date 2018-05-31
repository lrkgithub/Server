package Tomdog.springFrame.core;

import Tomdog.http.HttpRequest;
import Tomdog.http.HttpResponse;
import Tomdog.springFrame.ServletApi.HttpServlet;
import Tomdog.springFrame.annotation.Controller;
import Tomdog.springFrame.annotation.RequestMapping;
import Tomdog.springFrame.annotation.RequestParam;
import Tomdog.springFrame.aop.AopProxyUtils;
import Tomdog.springFrame.context.ApplicationContext;
import Tomdog.springFrame.mvc.HandlerAdapter;
import Tomdog.springFrame.mvc.HandlerMapping;
import Tomdog.springFrame.mvc.ModelAndView;
import Tomdog.springFrame.mvc.ViewResolver;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DispachServlet extends HttpServlet {
    
    private List<HandlerMapping> handlerMappings = new ArrayList<HandlerMapping>();

    private Map<HandlerMapping, HandlerAdapter> handlerAdapter = new HashMap<HandlerMapping, HandlerAdapter>();

    private List<ViewResolver> viewResolvers = new ArrayList<ViewResolver>();

    @Override
    public void init() {

        ApplicationContext applicationContext = new ApplicationContext("WEB-INF/Resource.properties"); 
        
        initStrategies(applicationContext);
    }

    @Override
    public void destory() {
//      TODO
    }

//    有九种策略
//    针对每个用户请求都会经过一些处理的策略之后，最终才能有结果输出
//    每种策略都可以自定义干预，但最终的结果都是一致的
//    ModelAndView
    private void initStrategies(ApplicationContext applicationContext) {
        
//        文件上传解析，如果是multipart将通过MultipartResolver进行文件上传解析。
        initMultipartResolver(applicationContext); 
        
//        本地化解析
        initLocalResolver(applicationContext);
        
//        主题解析
        initThemeResolver(applicationContext);
        
//        通过handlerMapping将请求映射到处理器，建立路径到方法的键值对。
        initHandlerMappings(applicationContext);
        
//        通过handlerAdapter进行多类型的动态参数匹配。
        initHandlerAdapter(applicationContext);
        
//        解析异常处理
        initHandlerExceptionResolver(applicationContext);
        
//        直接解析请求到视图名
        initRequestToViewNameTranslator(applicationContext);
        
//        实现动态模板解析
        initViewResolver(applicationContext);
        
//        flash映射管理器
        initFlashMapManager(applicationContext);
    }

    private void initMultipartResolver(ApplicationContext applicationContext) {
    }

    private void initLocalResolver(ApplicationContext applicationContext) {
    }

    private void initThemeResolver(ApplicationContext applicationContext) {
    }

    private void initHandlerMappings(ApplicationContext applicationContext) {

        String[] beanNames = applicationContext.getBeanDefinitionNames();

        for (String beanName : beanNames) {

            Object proxy = applicationContext.getBean(beanName);
            Object controller = null;
            try {
                controller = AopProxyUtils.getTargetObject(proxy);
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

            if (null == controller) {
                return;
            }

            Class<?> clazz = controller.getClass();

//            仅仅对controller进行处理，因为负责处理请求的只有被@Controller注释接口描述的类
            if (!clazz.isAnnotationPresent(Controller.class)) {
                continue;
            }

            String  baseUri = "";

            if (clazz.isAnnotationPresent(RequestMapping.class)) {
                RequestMapping annotation = clazz.getAnnotation(RequestMapping.class);
                baseUri = annotation.value();
            }

            Method[] methods = clazz.getMethods();

            for (Method method : methods) {

                if (!method.isAnnotationPresent(RequestMapping.class)) {
                    continue;
                }

                RequestMapping annotation = method.getAnnotation(RequestMapping.class);
                String regex = "/" + baseUri + annotation.value().replaceAll("\\*", ".*").replaceAll("/+", "/");

                Pattern pattern = Pattern.compile(regex);
                this.handlerMappings.add(new HandlerMapping(controller, method, pattern));
            }
        }
    }

    private void initHandlerAdapter(ApplicationContext applicationContext) {

        for (HandlerMapping handlerMapping : handlerMappings) {

            Map<String, Integer> paramMapping = new HashMap<String, Integer>();

            Annotation[][] annotationss = handlerMapping.getMethod().getParameterAnnotations();

            int index = -1;
            for (Annotation[] annotations : annotationss) {
                index++;
                for (Annotation annotation : annotations) {
                    if (annotation instanceof RequestParam) {
                        String paramName = ((RequestParam)annotation).value();
                        if (!"".equals(paramName.trim())) {
                            paramMapping.put(paramName, index);
                        }
                    }
                }
            }

            Class<?>[] paramTypes = handlerMapping.getMethod().getParameterTypes();

            index = -1;
            for (Class type : paramTypes) {
                index++;

                if (type == HttpRequest.class
                        || type == HttpResponse.class) {
                    paramMapping.put(type.getName(), index);
                }
            }

            this.handlerAdapter.put(handlerMapping, new HandlerAdapter(paramMapping));
        }
    }

    private void initHandlerExceptionResolver(ApplicationContext applicationContext) {
    }

    private void initRequestToViewNameTranslator(ApplicationContext applicationContext) {
    }

    private void initViewResolver(ApplicationContext applicationContext) {

//        页面和模板关联

        String templateRoot = applicationContext.getConfig().getProperty("templateRoot");
        URL resource = this.getClass().getClassLoader().getResource(templateRoot);

        if (null == resource) {
            return;
        }

        String templateRootPath = resource.getFile();

        File file = new File(templateRoot);

        if (!file.isDirectory()) {
            return;
        }

        File[] files = file.listFiles();

        for (File templateFile : files) {
            this.viewResolvers.add(new ViewResolver(templateFile.getName(), templateFile));
        }
    }

    private void initFlashMapManager(ApplicationContext applicationContext) {
    }

    @Override
    public void doGet(HttpRequest httpRequest, HttpResponse httpResponse) {
        try {
            doDispatch(httpRequest, httpResponse);
        } catch (Exception e) {
            httpResponse.bad();
            e.printStackTrace();
        }
    }

    private void doDispatch(HttpRequest httpRequest, HttpResponse httpResponse) throws Exception {

        HandlerMapping handlerMapping = getHandler(httpRequest);

        if (null == handlerMapping) {
            httpResponse.bad();
            return;
        }

        HandlerAdapter handlerAdapter = getAdapter(handlerMapping);

        if (null == handlerAdapter) {
            httpResponse.bad();
        }

        ModelAndView modelAndView = handlerAdapter.handle(httpRequest, httpResponse, handlerMapping);

    }

    private HandlerMapping getHandler(HttpRequest httpRequest) {

        if (this.handlerMappings.isEmpty()) {
            return null;
        }

        String url = httpRequest.getUri();
        String contextPath = httpRequest.getContextUri();

        url = url.replace(contextPath, "").replaceAll("/+", "/");

        for (HandlerMapping handlerMapping : this.handlerMappings) {
            Matcher matcher = handlerMapping.getPattern().matcher(url);

            if (!matcher.matches()) {
                return handlerMapping;
            }
        }

        return null;
    }

    private HandlerAdapter getAdapter(HandlerMapping handlerMapping) {

        if (this.handlerAdapter.isEmpty()) {
            return null;
        }

        return this.handlerAdapter.get(handlerMapping);
    }
}
