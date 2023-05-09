package cn.noobzz.gen.controller;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileNameUtil;
import cn.hutool.core.io.file.FileReader;
import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.util.StrUtil;
import cn.noobzz.gen.constant.DataSourceConstants;
import cn.noobzz.gen.domain.AjaxResult;
import cn.noobzz.gen.domain.GenTable;
import cn.noobzz.gen.domain.GenTableColumn;
import cn.noobzz.gen.domain.VelocityTemplate;
import cn.noobzz.gen.service.IGenTableColumnService;
import cn.noobzz.gen.service.IGenTableService;
import cn.noobzz.gen.util.DataSourceUtils;
import cn.noobzz.gen.util.VelocityUtils;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.dynamic.datasource.toolkit.DynamicDataSourceContextHolder;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.*;


/**
 * 代码生成 操作处理
 * 
 * @author ruoyi
 */
@Slf4j
@RequestMapping("/gen")
@RestController
public class GenController
{
    @Autowired
    private IGenTableService genTableService;

    @Autowired
    private IGenTableColumnService genTableColumnService;

    /***
     * @author ZZJ
     * @date 2022/10/23
     * @return cn.noobzz.gen.domain.AjaxResult
     * @desc  系统File流查询
     */
    @Deprecated
    @GetMapping("/getTemplateList")
    public AjaxResult getTemplateList(){
        File[] vms = FileUtil.ls("vm");
        List<Object> arrayList = new ArrayList<>();
        Map<String, Object> res = new HashMap<>();
        for(File file : vms){
            VelocityUtils.getTemplateTree(file,arrayList);
        }
        res.put("tree",arrayList);
        return AjaxResult.success(res);
    }

    @Deprecated
    @GetMapping("/getTemplate")
    public AjaxResult getTemplate(@RequestParam("templatePath") String templatePath){
        String templateContent = FileUtil.readUtf8String("vm/" + templatePath);
        return AjaxResult.success("查询失败",templateContent);
    }

    @PostMapping("/createTemplate")
    public AjaxResult createTemplate(@RequestBody VelocityTemplate template){
    String dir = FileUtil.getWebRoot().getAbsolutePath()+"/src/main/resources/vm/"+template.getDir();
    File vm = new File(dir);
    if (!FileUtil.exist(vm)) {
        vm = FileUtil.mkdir(dir);
    }
    File file = FileUtil.newFile(vm.getPath() + "/" + template.getName()+".vm");
    File utf8String = FileUtil.writeUtf8String(template.getContent(), file);
    FileUtil.copy(vm,FileUtil.file("vm"),true);
    return AjaxResult.success("新建成功",utf8String.getAbsolutePath());
    }

    @DeleteMapping("/delete")
    public AjaxResult deleteTemplate(@RequestParam("templatePath") String templatePath){

        boolean del = FileUtil.del("vm/" + templatePath);
        boolean del2 = FileUtil.del(FileUtil.getWebRoot().getAbsolutePath() + "/src/main/resources/vm/" + templatePath);
        if (FileUtil.isDirEmpty(FileUtil.file("vm/" + templatePath).getParentFile())){
            boolean del1 = FileUtil.del(FileUtil.file("vm/" + templatePath).getParentFile());
            boolean del3 = FileUtil.del(FileUtil.file(FileUtil.getWebRoot().getAbsolutePath() + "/src/main/resources/vm/" + templatePath).getParentFile());
            return AjaxResult.success(del && del1 && del2 && del3);
        }

        return AjaxResult.success(del && del2);
    }

    /**
     * 查询代码生成列表
     */
    @GetMapping("/list")
    public AjaxResult genList(@RequestParam(value = "pageNum",required = false) String pageNum,
                              @RequestParam(value = "pageSize",required = false) String pageSize, GenTable genTable)
    {
        PageHelper.startPage(Convert.toInt(pageNum),Convert.toInt(pageSize));
        List<GenTable> list = genTableService.selectGenTableList(genTable);
        log.info(list.toString());
        return AjaxResult.success(new PageInfo<>(list));
    }

    /**
     * 修改代码生成业务
     */
    @GetMapping(value = "/{tableId}")
    public AjaxResult getInfo(@PathVariable Long tableId)
    {
        GenTable table = genTableService.selectGenTableById(tableId);
        List<GenTable> tables = genTableService.selectGenTableAll();
        List<GenTableColumn> list = genTableColumnService.selectGenTableColumnListByTableId(tableId);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("info", table);
        map.put("rows", list);
        map.put("tables", tables);
        return AjaxResult.success(map);
    }

    /**
     * 查询数据库列表
     */
    @GetMapping("/db/list")
    public AjaxResult dataList(@RequestParam(value = "pageNum",required = false) String pageNum,
                               @RequestParam(value = "pageSize",required = false) String pageSize,
                               @RequestParam(value = "datasource",required = false) String datasource,
                               GenTable genTable)
    {
        if (datasource != null){
            genTable.setOptions(datasource);
        }
        PageHelper.startPage(Convert.toInt(pageNum),Convert.toInt(pageSize));
        List<GenTable> list = genTableService.selectDbTableList(genTable);
        return AjaxResult.success(new PageInfo<>(list));
    }

    /**
     * 查询数据表字段列表
     */
    @GetMapping(value = "/column/{tableId}")
    public AjaxResult columnList(Long tableId)
    {
        List<GenTableColumn> list = genTableColumnService.selectGenTableColumnListByTableId(tableId);
        return AjaxResult.success(list);
    }

    /**
     * 导入表结构（保存）
     */
    @PostMapping("/importTable")
    public AjaxResult importTableSave(String datasource,String tables,String templateSelector)
    {
        String[] tableNames = Convert.toStrArray(tables);
        // 查询表信息
        List<GenTable> tableList = DataSourceUtils.switchDatasource(datasource, () -> genTableService.selectDbTableListByNames(tableNames));
        tableList.forEach(each -> each.setTemplateSelector(templateSelector));
        genTableService.importGenTable(datasource,tableList);
        return AjaxResult.success();
    }

    /**
     * 修改保存代码生成业务
     */
    @PutMapping
    public AjaxResult editSave(@Validated @RequestBody GenTable genTable)
    {
        genTableService.validateEdit(genTable);
        genTableService.updateGenTable(genTable);
        return AjaxResult.success();
    }

    /**
     * 删除代码生成
     */
    @DeleteMapping("/{tableIds}")
    public AjaxResult remove(@PathVariable Long[] tableIds)
    {
        genTableService.deleteGenTableByIds(tableIds);
        return AjaxResult.success();
    }

    /**
     * 预览代码
     */
    @GetMapping("/preview/{tableId}")
    public AjaxResult preview(@PathVariable("tableId") Long tableId) throws IOException
    {
        Map<String, String> dataMap = genTableService.previewCode(tableId);
        return AjaxResult.success(dataMap);
    }

    @GetMapping("/customPreview/{tableId}")
    public AjaxResult customPreview(@PathVariable("tableId") Long tableId) throws IOException
    {
        Map<String, String> dataMap = genTableService.customPreviewCode(tableId);
        return AjaxResult.success(dataMap);
    }

    /**
     * 生成代码（下载方式）
     */
    @GetMapping("/download/{tableName}")
    public void download(HttpServletResponse response, @PathVariable("tableName") String tableName) throws IOException
    {
        byte[] data = genTableService.downloadCode(tableName);
        genCode(response, data);
    }

    /**
     * 生成代码（下载方式）
     */
    @PostMapping("/movePathWithCode")
    public AjaxResult movePathWithCode(@RequestBody String post) throws IOException
    {
        JSONObject jsonObject = JSONObject.parseObject(post);
        Long tableId = Long.parseLong((String) jsonObject.get("tableId"));
        String template = (String) jsonObject.get("template");
        String movePath = (String) jsonObject.get("movePath");
        String path = genTableService.movePathWithCode(movePath, template, tableId);
        return AjaxResult.success("移动成功",path);
    }


    /**
     * 生成代码（自定义路径）
     */
    @GetMapping("/genCode/{tableName}")
    public AjaxResult genCode(@PathVariable("tableName") String tableName)
    {
        genTableService.generatorCode(tableName);
        return AjaxResult.success();
    }

    /**
     * 同步数据库
     */
    @GetMapping("/synchDb/{tableName}")
    public AjaxResult synchDb(@PathVariable("tableName") String tableName)
    {
        genTableService.synchDb(tableName);
        return AjaxResult.success();
    }

    /**
     * 批量生成代码
     */
    @GetMapping("/batchGenCode")
    public void batchGenCode(HttpServletResponse response, String tables) throws IOException
    {
        String[] tableNames = Convert.toStrArray(tables);
        byte[] data = genTableService.downloadCode(tableNames);
        genCode(response, data);
    }

    /**
     * 生成zip文件
     */
    private void genCode(HttpServletResponse response, byte[] data) throws IOException
    {
        response.reset();
        response.setHeader("Content-Disposition", "attachment; filename=\"noobzz-codeGenerator.zip\"");
        response.addHeader("Content-Length", "" + data.length);
        response.setContentType("application/octet-stream; charset=UTF-8");
        IOUtils.write(data, response.getOutputStream());
    }
}
