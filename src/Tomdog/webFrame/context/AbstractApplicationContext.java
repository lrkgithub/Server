package Tomdog.webFrame.context;

public abstract class AbstractApplicationContext implements ConfigurableApplicationContext {

//    提供给子类重写
    protected void onRefresh() {}

}
