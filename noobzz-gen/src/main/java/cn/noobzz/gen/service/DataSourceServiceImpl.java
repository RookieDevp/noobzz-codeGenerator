package cn.noobzz.gen.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.noobzz.gen.constant.DataSourceConstants;
import cn.noobzz.gen.domain.AjaxResult;
import cn.noobzz.gen.domain.DataSource;
import cn.noobzz.gen.domain.GenTableColumn;
import cn.noobzz.gen.mapper.IDataSourceMapper;
import cn.noobzz.gen.util.DataSourceUtils;
import com.alibaba.druid.pool.DruidDataSource;
import com.baomidou.dynamic.datasource.DynamicRoutingDataSource;
import com.baomidou.dynamic.datasource.ds.ItemDataSource;
import com.baomidou.dynamic.datasource.toolkit.DynamicDataSourceContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static net.sf.jsqlparser.parser.feature.Feature.values;

/**
 * @author: ZZJ
 * @date: 2022/10/30
 * @desc:
 */
@Service
@Slf4j
public class DataSourceServiceImpl implements IDataSourceService {

    @Resource
    private javax.sql.DataSource dynamicDataSource;

    @Autowired
    private IDataSourceMapper datasourceMapper;

    @PostConstruct
    void init(){
        initDataSources();
    }

    @Override
    public void initDataSources() {
        DataSource source = new DataSource();
        source.setStatus("0");
        List<DataSource> dataSources = datasourceMapper.selectDatasourceList(source);

        for (DataSource dataSource : dataSources) {
            try {
                addDynamicDataSource(dataSource);
            }catch (Exception e){
            }
        }
        DynamicRoutingDataSource ds = (DynamicRoutingDataSource) dynamicDataSource;
        Map<String, javax.sql.DataSource> dynamicDBs = ds.getDataSources();
        log.info("{}初始化动态数据源{}个",dynamicDBs.keySet().toArray(),dynamicDBs.size());
    }

    @Override
    public boolean testConnection(DataSource source) {
        boolean b = DataSourceUtils.testConnection(source);
        return b;
    }

    private void addDynamicDataSource(DataSource dataSource){
        DynamicRoutingDataSource ds = (DynamicRoutingDataSource) dynamicDataSource;
        boolean b = DataSourceUtils.testConnection(dataSource);
        if (!b){
            log.info("{}-------->连接失败",dataSource.toString());
            throw new IllegalArgumentException(dataSource.getConnectionName()+"数据库连接失败,添加数据源失败！");
        }
        DruidDataSource druidDataSource = new DruidDataSource();
        druidDataSource.setUrl(dataSource.getUrl());
        druidDataSource.setName(dataSource.getConnectionName());
        druidDataSource.setUsername(dataSource.getUsername());
        druidDataSource.setPassword(dataSource.getPassword());
        druidDataSource.setDriverClassName(DataSourceConstants.MYSQL_CLASS_DRIVER);
        ds.addDataSource(dataSource.getConnectionName(),druidDataSource);
    }

    /**
     * 查询数据源
     *
     * @param datasourceId 数据源主键
     * @return 数据源
     */
    @Override
    public DataSource selectDatasourceByDatasourceId(String datasourceId)
    {
        return datasourceMapper.selectDatasourceByDatasourceId(datasourceId);
    }

    /**
     * 查询数据源列表
     *
     * @param datasource 数据源
     * @return 数据源
     */
    @Override
    public List<DataSource> selectDatasourceList(DataSource datasource)
    {
        return datasourceMapper.selectDatasourceList(datasource);
    }

    /**
     * 新增数据源
     *
     * @param dataSource 数据源
     * @return 结果
     */
    @Override
    public int insertDatasource(DataSource dataSource)
    {
        dataSource.setCreateTime(DateUtil.date());
        boolean b = DataSourceUtils.testConnection(dataSource);
        if (!b){
            dataSource.setStatus("1");
        }
        int i = datasourceMapper.insertDatasource(dataSource);
        if (i > 0){
            try {
                addDynamicDataSource(dataSource);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return i;
    }

    /**
     * 修改数据源
     *
     * @param datasource 数据源
     * @return 结果
     */
    @Override
    public int updateDatasource(DataSource datasource)
    {
        datasource.setUpdateTime(DateUtil.date());

        boolean b = DataSourceUtils.testConnection(datasource);
        if (!b){
            datasource.setStatus("1");
        }
        int i = datasourceMapper.updateDatasource(datasource);
        if (i > 0){
            try {
                addDynamicDataSource(datasource);
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        return i;
    }

    /**
     * 批量删除数据源
     *
     * @param datasourceIds 需要删除的数据源主键
     * @return 结果
     */
    @Override
    public int deleteDatasourceByDatasourceIds(String[] datasourceIds)
    {
        return datasourceMapper.deleteDatasourceByDatasourceIds(datasourceIds);
    }

    /**
     * 删除数据源信息
     *
     * @param datasourceId 数据源主键
     * @return 结果
     */
    @Override
    public int deleteDatasourceByDatasourceId(String datasourceId)
    {
        return datasourceMapper.deleteDatasourceByDatasourceId(datasourceId);
    }
}
