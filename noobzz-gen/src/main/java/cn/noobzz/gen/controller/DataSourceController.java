package cn.noobzz.gen.controller;

import cn.hutool.core.convert.Convert;
import cn.noobzz.gen.domain.AjaxResult;
import cn.noobzz.gen.domain.DataSource;
import cn.noobzz.gen.domain.Template;
import cn.noobzz.gen.service.IDataSourceService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;

/**
 * @author: ZZJ
 * @date: 2022/10/30
 * @desc:
 */
@RestController
@RequestMapping("/datasource")
public class DataSourceController {

    @Autowired
    private IDataSourceService dataSourceService;

    @PostMapping("/test")
    public AjaxResult testConnection(@RequestBody DataSource source){

        try {
            Connection connection = DriverManager.getConnection(source.getUrl(), source.getUsername(), source.getPassword());
            if (connection == null){
                return AjaxResult.error("数据库连接失败");
            }
        }catch (Exception e){
            e.printStackTrace();
            return AjaxResult.error("数据库连接失败");
        }
        return AjaxResult.success("数据库连接成功");
    }

    /**
     * 查询数据源列表
     */
    @GetMapping("/list")
    public AjaxResult list(@RequestParam(value = "pageNum",required = false) String pageNum,
                           @RequestParam(value = "pageSize",required = false) String pageSize,DataSource source)
    {
        PageHelper.startPage(Convert.toInt(pageNum), Convert.toInt(pageSize));
        List<DataSource> list = dataSourceService.selectDatasourceList(source);
        return AjaxResult.success(new PageInfo<>(list));
    }

    /**
     * 导出数据源列表
     */
//    @PostMapping("/export")
//    public void export(HttpServletResponse response, DataSource source)
//    {
//        List<Datasource> list = datasourceService.selectDatasourceList(datasource);
//        ExcelUtil<Datasource> util = new ExcelUtil<Datasource>(Datasource.class);
//        util.exportExcel(response, list, "数据源数据");
//    }

    /**
     * 获取数据源详细信息
     */
    @GetMapping(value = "/{datasourceId}")
    public AjaxResult getInfo(@PathVariable("datasourceId") String datasourceId)
    {
        return AjaxResult.success(dataSourceService.selectDatasourceByDatasourceId(datasourceId));
    }

    /**
     * 新增数据源
     */
    @PostMapping
    public AjaxResult add(@RequestBody DataSource datasource)
    {
        return AjaxResult.success(dataSourceService.insertDatasource(datasource));
    }

    /**
     * 修改数据源
     */
    @PutMapping
    public AjaxResult edit(@RequestBody DataSource datasource)
    {
        return AjaxResult.success(dataSourceService.updateDatasource(datasource));
    }

    /**
     * 删除数据源
     */
    @DeleteMapping("/{datasourceIds}")
    public AjaxResult remove(@PathVariable String[] datasourceIds)
    {
        return AjaxResult.success(dataSourceService.deleteDatasourceByDatasourceIds(datasourceIds));
    }
}
