package com.skillbox.engine.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class DefaultController {

    @GetMapping("/")
    public String index() {
        return "index";
    }


    @RequestMapping(
            method = {
                    RequestMethod.OPTIONS,
                    RequestMethod.GET},
            value = "/**/{path:[^\\\\.]+}")// ""/**/{path:[^\\\\.]*}")
    public String redirectToIndex(@PathVariable String path) {
        return "forward:/";
    }
}
