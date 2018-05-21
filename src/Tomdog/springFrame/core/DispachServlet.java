package Tomdog.springFrame.core;

import Tomdog.springFrame.ServletApi.HttpServlet;
import Tomdog.springFrame.aop.AopProxyUtils;
import Tomdog.springFrame.context.ApplicationContext;

public class DispachServlet extends HttpServlet {
    
    

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
            Object controller = AopProxyUtils.getTargetObject(proxy);
            Class<?> clazz = controller.getClass();

//            TODO

        }

    }

    private void initHandlerAdapter(ApplicationContext applicationContext) {
    }

    private void initHandlerExceptionResolver(ApplicationContext applicationContext) {
    }

    private void initRequestToViewNameTranslator(ApplicationContext applicationContext) {
    }

    private void initViewResolver(ApplicationContext applicationContext) {
    }

    private void initFlashMapManager(ApplicationContext applicationContext) {
    }

}
