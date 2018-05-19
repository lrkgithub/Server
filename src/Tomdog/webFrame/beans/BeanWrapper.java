package Tomdog.webFrame.beans;

public class BeanWrapper {

    private BeanPostProcessor postPostProcessor;

    private Object wrapperInstance;
    private Object instance;

    public BeanWrapper(Object instance) {
        this.instance = instance;
    }

    public Object getInstance() {
        return instance;
    }

    public BeanPostProcessor getPostPostProcessor() {
        return postPostProcessor;
    }

    public void setPostPostProcessor(BeanPostProcessor postPostProcessor) {
        this.postPostProcessor = postPostProcessor;
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
