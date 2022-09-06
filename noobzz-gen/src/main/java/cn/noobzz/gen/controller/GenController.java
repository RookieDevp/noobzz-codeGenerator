package cn.noobzz.gen.controller;

import cn.hutool.core.convert.Convert;
import cn.noobzz.gen.domain.AjaxResult;
import cn.noobzz.gen.domain.GenTable;
import cn.noobzz.gen.domain.GenTableColumn;
import cn.noobzz.gen.service.IGenTableColumnService;
import cn.noobzz.gen.service.IGenTableService;
import cn.noobzz.gen.util.VelocityUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 代码生成 操作处理
 * 
 * @author ruoyi
 */
@RequestMapping("/gen")
@RestController
public class GenController
{
    @Autowired
    private IGenTableService genTableService;

    @Autowired
    private IGenTableColumnService genTableColumnService;

    @GetMapping("/getTemplateList/{tpl}")
    public List<String> getTemplateList(@PathVariable String tpl){
        List<String> templates = VelocityUtils.getTemplateList(tpl);
        return templates;
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
                               @RequestParam(value = "pageSize",required = false) String pageSize, GenTable genTable)
    {
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
    public AjaxResult importTableSave(String tables)
    {
        String[] tableNames = Convert.toStrArray(tables);
        // 查询表信息
        List<GenTable> tableList = genTableService.selectDbTableListByNames(tableNames);
        genTableService.importGenTable(tableList);
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
