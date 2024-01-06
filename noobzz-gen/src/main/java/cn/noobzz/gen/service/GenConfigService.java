package cn.noobzz.gen.service;

import cn.noobzz.gen.domain.GenConfig;

import java.util.List;

/**
 * @author ZZJ
 * @date 2024/01/06
 * @desc
 */
public interface GenConfigService{

    /**
     * 查询生成配置
     *
     * @param id 生成配置主键
     * @return 生成配置
     */
    public GenConfig selectGenConfigById(Long id);

    /**
     * 查询生成配置列表
     *
     * @param GenConfig 生成配置
     * @return 生成配置集合
     */
    public List<GenConfig> selectGenConfigList(GenConfig GenConfig);

    /**
     * 新增生成配置
     *
     * @param GenConfig 生成配置
     * @return 结果
     */
    public int insertGenConfig(GenConfig GenConfig);

    /**
     * 修改生成配置
     *
     * @param GenConfig 生成配置
     * @return 结果
     */
    public int updateGenConfig(GenConfig GenConfig);

    /**
     * 批量删除生成配置
     *
     * @param ids 需要删除的生成配置主键集合
     * @return 结果
     */
    public int deleteGenConfigByIds(Long[] ids);

    /**
     * 删除生成配置信息
     *
     * @param id 生成配置主键
     * @return 结果
     */
    public int deleteGenConfigById(Long id);
}
