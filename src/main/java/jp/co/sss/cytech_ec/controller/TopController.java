package jp.co.sss.cytech_ec.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TopController {

    @GetMapping("/top")
    public String showTop() {
        return "top";
    }
}
