package cn.noobzz.gen.service;

import cn.noobzz.gen.domain.DataSource;

import java.util.List;

/**
 * @author: ZZJ
 * @date: 2022/10/30
 * @desc:
 */
public interface IDataSourceService {

    /***
     * @author ZZJ
     * @date 2022/10/30
     * @desc  初始化数据源
     */
    void initDataSources();

    boolean testConnection(DataSource source);

    /**
     * 查询数据源
     *
     * @param datasourceId 数据源主键
     * @return 数据源
     */
    public DataSource selectDatasourceByDatasourceId(String datasourceId);

    /**
     * 查询数据源列表
     *
     * @param datasource 数据源
     * @return 数据源集合
     */
    public List<DataSource> selectDatasourceList(DataSource datasource);

    /**
     * 新增数据源
     *
     * @param datasource 数据源
     * @return 结果
     */
    public int insertDatasource(DataSource datasource);

    /**
     * 修改数据源
     *
     * @param datasource 数据源
     * @return 结果
     */
    public int updateDatasource(DataSource datasource);

    /**
     * 批量删除数据源
     *
     * @param datasourceIds 需要删除的数据源主键集合
     * @return 结果
     */
    public int deleteDatasourceByDatasourceIds(String[] datasourceIds);

    /**
     * 删除数据源信息
     *
     * @param datasourceId 数据源主键
     * @return 结果
     */
    public int deleteDatasourceByDatasourceId(String datasourceId);
}
