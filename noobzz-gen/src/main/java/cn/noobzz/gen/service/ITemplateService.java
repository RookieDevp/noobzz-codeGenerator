package cn.noobzz.gen.service;

import cn.noobzz.gen.domain.Template;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author: ZZJ
 * @date: 2022/09/15
 * @desc:
 */
public interface ITemplateService {

    /**
     * 查询模板信息
     *
     * @param templateId 模板信息主键
     * @return 模板信息
     */
    public Template selectTemplateByTemplateId(String templateId);

    /**
     * 查询模板信息列表
     *
     * @param template 模板信息
     * @return 模板信息集合
     */
    public List<Template> selectTemplateList(Template template);

    /**
     * 新增模板信息
     *
     * @param template 模板信息
     * @return 结果
     */
    public int insertTemplate(Template template);

    /**
     * 修改模板信息
     *
     * @param template 模板信息
     * @return 结果
     */
    public int updateTemplate(Template template);

    /**
     * 批量删除模板信息
     *
     * @param templateIds 需要删除的模板信息主键集合
     * @return 结果
     */
    public int deleteTemplateByTemplateIds(String[] templateIds);

    /**
     * 删除模板信息信息
     *
     * @param templateId 模板信息主键
     * @return 结果
     */
    public int deleteTemplateByTemplateId(String templateId);

    public void importTemplate(List<Template> templates);

    public List<Template> selectTemplateListByIds(String[] ids);

    public List<Template> selectTemplateListByDynamic(Map<String,Object> map);

    public int importTemplateByFile(MultipartFile file);
}
