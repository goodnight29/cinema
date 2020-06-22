package com.qhit.fsaas.controller;

import com.qhit.fsaas.util.MainUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.annotation.Resource;

/**
 * @author jieyue-mac
 */
@Controller
public class HomeController {
    @Resource
    MainUtil mainUtil;

    /**
     * 系统首页
     *
     * @return 主页
     */
    @GetMapping(value = {"/", "/index"})
    public String index() {
        mainUtil.init();
        return "index";
    }

    @GetMapping(value = {"/seat"})
    public String seat() {
        return "seatPreview/index";
    }

    @GetMapping(value = {"/air/A33A"})
    public String airA33A() {
        mainUtil.init();
        return "aircraft/A33A";
    }

    @GetMapping(value = {"/demo"})
    public String demo() {
        return "demo";
    }

}
