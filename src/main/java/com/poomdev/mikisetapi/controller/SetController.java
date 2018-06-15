package com.poomdev.mikisetapi.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/set")
public class SetController {

    @RequestMapping(name = "", method = RequestMethod.GET)
    public String swaggerUI() {
        return "redirect:/swagger-ui.html";
    }

}
