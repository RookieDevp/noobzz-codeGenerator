package cn.noobzz.gen.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: ZZJ
 * @date: 2022/09/03
 * @desc:
 */
@RestController
@RequestMapping("/")
public class Test {

    @RequestMapping("/test")
    public String test(){
        return "test";
    }
}
