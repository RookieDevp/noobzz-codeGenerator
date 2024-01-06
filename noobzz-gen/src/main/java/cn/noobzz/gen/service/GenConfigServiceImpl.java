package cn.noobzz.gen.service;

import cn.noobzz.gen.domain.GenConfig;
import cn.noobzz.gen.mapper.GenConfigMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author ZZJ
 * @date 2024/01/06
 * @desc
 */
@Service
public class GenConfigServiceImpl implements GenConfigService{

    @Autowired
    private GenConfigMapper GenConfigMapper;

    /**
     * 查询生成配置
     *
     * @param id 生成配置主键
     * @return 生成配置
     */
    @Override
    public GenConfig selectGenConfigById(Long id)
    {
        return GenConfigMapper.selectGenConfigById(id);
    }

    /**
     * 查询生成配置列表
     *
     * @param GenConfig 生成配置
     * @return 生成配置
     */
    @Override
    public List<GenConfig> selectGenConfigList(GenConfig GenConfig)
    {
        return GenConfigMapper.selectGenConfigList(GenConfig);
    }

    /**
     * 新增生成配置
     *
     * @param GenConfig 生成配置
     * @return 结果
     */
    @Override
    public int insertGenConfig(GenConfig GenConfig)
    {
        return GenConfigMapper.insertGenConfig(GenConfig);
    }

    /**
     * 修改生成配置
     *
     * @param GenConfig 生成配置
     * @return 结果
     */
    @Override
    public int updateGenConfig(GenConfig GenConfig)
    {
        return GenConfigMapper.updateGenConfig(GenConfig);
    }

    /**
     * 批量删除生成配置
     *
     * @param ids 需要删除的生成配置主键
     * @return 结果
     */
    @Override
    public int deleteGenConfigByIds(Long[] ids)
    {
        return GenConfigMapper.deleteGenConfigByIds(ids);
    }

    /**
     * 删除生成配置信息
     *
     * @param id 生成配置主键
     * @return 结果
     */
    @Override
    public int deleteGenConfigById(Long id)
    {
        return GenConfigMapper.deleteGenConfigById(id);
    }
}
