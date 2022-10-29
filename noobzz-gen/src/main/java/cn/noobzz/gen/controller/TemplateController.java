package cn.noobzz.gen.controller;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ByteUtil;
import cn.noobzz.gen.constant.TemplateConstants;
import cn.noobzz.gen.domain.AjaxResult;
import cn.noobzz.gen.domain.GenTable;
import cn.noobzz.gen.domain.Template;
import cn.noobzz.gen.service.ITemplateService;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author: ZZJ
 * @date: 2022/09/15
 * @desc:
 */
@RestController
@RequestMapping("/template")
public class TemplateController {

    @Autowired
    private ITemplateService templateService;

    /**
     * 查询模板信息列表
     */
    @GetMapping("/list")
    public AjaxResult list(@RequestParam(value = "pageNum",required = false) String pageNum,
                              @RequestParam(value = "pageSize",required = false) String pageSize,Template template)
    {
        PageHelper.startPage(Convert.toInt(pageNum),Convert.toInt(pageSize));
        List<Template> list = templateService.selectTemplateList(template);
        return AjaxResult.success(new PageInfo<>(list));
    }

    /**
     * 导入模板（保存）
     */
    @PostMapping("/importTemplate/{templateIds}")
    public AjaxResult importTemplate(@PathVariable("templateIds") String[] ids)
    {
        String[] templateIds = Convert.toStrArray(ids);
        List<Template> templates = templateService.selectTemplateListByIds(templateIds);

        templateService.importTemplate(templates);
        return AjaxResult.success();
    }

    /***
     * @author ZZJ
     * @date 2022/9/19
     * @param post
     * @return cn.noobzz.gen.domain.AjaxResult
     * @desc    动态查询
     */
    @PostMapping("/getTemplateDynamic")
    public AjaxResult getTemplateDynamic(@RequestBody String post){
        Map map = JSON.parseObject(post, Map.class);
        List<Template> templates = templateService.selectTemplateListByDynamic(map);
        PageInfo<Template> templatePageInfo = new PageInfo<>(templates);
        int size = templates.stream().filter(item -> TemplateConstants.TYPE_FILE.equals(item.getTemplateType())).collect(Collectors.toList()).size();
        templatePageInfo.setSize(size);
        return AjaxResult.success(templatePageInfo);
    }

    /***
     * @author ZZJ
     * @date 2022/10/23
     * @param multipartFile
     * @return cn.noobzz.gen.domain.AjaxResult
     * @desc  模板导入上传
     */
    @PostMapping("/uploadTemplate")
    public AjaxResult uploadTemplate(@RequestParam("file") MultipartFile multipartFile){
        HashMap<Object, Object> objectObjectHashMap = new HashMap<>();
        int i = templateService.importTemplateByFile(multipartFile);
        return AjaxResult.success("上传成功",objectObjectHashMap);
    }
    /**
     * 获取模板信息详细信息
     */
    @GetMapping(value = "/{templateId}")
    public AjaxResult getInfo(@PathVariable("templateId") String templateId)
    {
        return AjaxResult.success(templateService.selectTemplateByTemplateId(templateId));
    }

    /**
     * 新增模板信息 ON
     */
    @PostMapping
    public AjaxResult add(@RequestBody Template template)
    {
        return AjaxResult.success(templateService.insertTemplate(template));
    }

    /**
     * 修改模板信息
     */
    @PutMapping
    public AjaxResult edit(@RequestBody Template template)
    {
        return AjaxResult.success(templateService.updateTemplate(template));
    }

    /**
     * 删除模板信息
     */
    @DeleteMapping("/{templateIds}")
    public AjaxResult remove(@PathVariable String[] templateIds)
    {
        return AjaxResult.success(templateService.deleteTemplateByTemplateIds(templateIds));
    }

}
