package com.example.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

public class CorsFilter {

    @RequestMapping(method = RequestMethod.OPTIONS)
    public void commonOptions(HttpServletResponse theHttpServletResponse) throws IOException {
        theHttpServletResponse.addHeader("Access-Control-Allow-Headers", "origin, content-type, accept, x-requested-with, my-cool-header");
        theHttpServletResponse.addHeader("Access-Control-Max-Age", "60"); // seconds to cache preflight request --> less OPTIONS traffic
        theHttpServletResponse.addHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
        theHttpServletResponse.addHeader("Access-Control-Allow-Origin", "*");
    }
}
