package cn.noobzz.gen.controller;

import cn.noobzz.gen.config.GenConfigComponent;
import cn.noobzz.gen.domain.AjaxResult;
import cn.noobzz.gen.domain.GenConfig;
import cn.noobzz.gen.service.GenConfigServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

/**
 * @author: ZZJ
 * @date: 2023/05/09
 * @desc:
 */
@RestController
@RequestMapping("/sysConfig")
public class SysConfigController {

    @Autowired
    private GenConfigComponent config;

    @Autowired
    private GenConfigServiceImpl configService;

    @GetMapping("/getInit")
    public AjaxResult getInitGenConfig() {
        GenConfig one = configService.selectGenConfigById(1L);
        config.setAuthor(one.getAuthor());
        config.setAutoRemovePre(one.isAutoRemovePre());
        config.setPackageName(one.getPackageName());
        config.setTablePrefix(one.getTablePrefix());
        return AjaxResult.success(one);
    }

    @GetMapping("/get")
    public AjaxResult getGenConfig(){
        GenConfig one = configService.selectGenConfigById(1L);
        return AjaxResult.success(one);
    }

    @PutMapping("/edit")
    public AjaxResult editGenConfig(@RequestBody GenConfig genConfig){
        int i = configService.updateGenConfig(genConfig);
        if (i < 0){
            return AjaxResult.error();
        }
        config.setAuthor(genConfig.getAuthor());
        config.setAutoRemovePre(genConfig.isAutoRemovePre());
        config.setPackageName(genConfig.getPackageName());
        config.setTablePrefix(genConfig.getTablePrefix());
        return AjaxResult.success();
    }


}
