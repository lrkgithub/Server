package demo;

import Tomdog.springFrame.annotation.Autoware;
import Tomdog.springFrame.annotation.Controller;
import Tomdog.springFrame.annotation.RequestMapping;

@Controller
@RequestMapping("/lrk")
public class testResource {

    @Autoware()
    private a a1;

}
