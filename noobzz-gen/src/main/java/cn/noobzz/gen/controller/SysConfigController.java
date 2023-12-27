//package cn.noobzz.gen.controller;
//
//import cn.hutool.core.lang.Dict;
//import cn.hutool.core.map.MapUtil;
//import cn.hutool.core.util.StrUtil;
//import cn.hutool.setting.yaml.YamlUtil;
//import cn.noobzz.gen.config.GenConfig;
//import cn.noobzz.gen.domain.AjaxResult;
//import com.alibaba.cloud.nacos.NacosConfigManager;
//import com.alibaba.cloud.nacos.NacosConfigProperties;
//import com.alibaba.fastjson2.JSONObject;
//import com.alibaba.nacos.api.NacosFactory;
//import com.alibaba.nacos.api.config.ConfigFactory;
//import com.alibaba.nacos.api.config.ConfigService;
//import com.alibaba.nacos.api.exception.NacosException;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//import org.yaml.snakeyaml.Yaml;
//
//import java.util.HashMap;
//import java.util.Map;
//import java.util.Properties;
//
///**
// * @author: ZZJ
// * @date: 2023/05/09
// * @desc:
// */
//@RestController
//@RequestMapping("/sysConfig")
//public class SysConfigController {
//
//    @Autowired
//    private GenConfig genConfig;
//
//    @GetMapping("/getInit")
//    public AjaxResult getInitGenConfig() throws NacosException {
//        ConfigService configService = new NacosConfigProperties().configServiceInstance();
////        ConfigService configService = NacosFactory.createConfigService("175.178.240.180:8848");
//        String config = configService.getConfig("noobzz-gen-dev.yml", "DEFAULT_GROUP", 5000);
//        Dict load = YamlUtil.load(StrUtil.getReader(config));
//        Dict gen = Dict.parse(load.get("gen"));
//        genConfig.setAuthor(gen.getStr("author"));
//        genConfig.setAutoRemovePre(gen.getBool("autoRemovePre"));
//        genConfig.setPackageName(gen.getStr("packageName"));
//        genConfig.setTablePrefix(gen.getStr("tablePrefix"));
//        return AjaxResult.success();
//    }
//
//    @GetMapping("/get")
//    public AjaxResult getGenConfig(){
//        HashMap<String, Object> map = new HashMap<>();
//        map.put("packageName",GenConfig.getPackageName());
//        map.put("autoRemovePre",GenConfig.getAutoRemovePre());
//        map.put("author",GenConfig.getAuthor());
//        map.put("tablePrefix",GenConfig.getTablePrefix());
//        return AjaxResult.success(map);
//    }
//
//    @PutMapping("/edit")
//    public AjaxResult editGenConfig(@RequestBody String req){
//        JSONObject parseObject = JSONObject.parseObject(req);
//        genConfig.setAuthor(parseObject.getString("author"));
//        genConfig.setAutoRemovePre(Boolean.parseBoolean(parseObject.getString("autoRemovePre")));
//        genConfig.setPackageName(parseObject.getString("packageName"));
//        genConfig.setTablePrefix(parseObject.getString("tablePrefix"));
//        return AjaxResult.success();
//    }
//
//
//}
