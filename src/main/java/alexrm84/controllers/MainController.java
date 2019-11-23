package alexrm84.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MainController {

    @GetMapping("/")
    public String showIndex(){
        return "index";
    }

    @GetMapping("/login")
    public String login(){
        return "login";
    }
}
