package demo;

import Tomdog.webFrame.annotation.Autoware;
import Tomdog.webFrame.annotation.Controller;
import Tomdog.webFrame.annotation.RequestMapping;

@Controller
@RequestMapping("/lrk")
public class testResource {

    @Autoware()
    private a a1;

}
