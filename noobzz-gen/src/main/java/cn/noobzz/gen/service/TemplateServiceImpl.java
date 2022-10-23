package cn.noobzz.gen.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileTypeUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.noobzz.gen.constant.TemplateConstants;
import cn.noobzz.gen.domain.AjaxResult;
import cn.noobzz.gen.domain.Template;
import cn.noobzz.gen.mapper.ITemplateMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author: ZZJ
 * @date: 2022/09/15
 * @desc:
 */
@Service
public class TemplateServiceImpl implements ITemplateService {

    @Autowired
    private ITemplateMapper templateMapper;

    /**
     * 查询模板信息
     *
     * @param templateId 模板信息主键
     * @return 模板信息
     */
    @Override
    public Template selectTemplateByTemplateId(String templateId) {
        return templateMapper.selectTemplateByTemplateId(templateId);
    }

    /**
     * 查询模板信息列表
     *
     * @param template 模板信息
     * @return 模板信息
     */
    @Override
    public List<Template> selectTemplateList(Template template) {
        return templateMapper.selectTemplateList(template);
    }

    /**
     * 新增模板信息
     *
     * @param template 模板信息
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertTemplate(Template template) {
        int i = 0;
        try {
            Template parentTemplate = templateMapper.selectTemplateByTemplateId(template.getParentId());

            String parentPath = parentTemplate.getPath();
            template.setCreateTime(DateUtil.date());
            if (TemplateConstants.TYPE_MENU.equals(template.getTemplateType())) {

                String dir = FileUtil.getWebRoot().getAbsolutePath() + "/" + TemplateConstants.SRC_MAIN_RESOURCES + "/" + parentPath;
                File vm = new File(dir);
                if (!FileUtil.exist(vm)) {
                    vm = FileUtil.mkdir(dir);
                }
                if (FileUtil.file(vm.getPath() + "/" + template.getTemplateName()).exists()) {
                    throw new IllegalArgumentException("目录存在");
                }
                File mkdir1 = FileUtil.mkdir(vm.getPath() + "/" + template.getTemplateName());
                File mkdir = FileUtil.mkdir(parentPath+ "/" + template.getTemplateName());
                template.setPath(parentPath + "/" + template.getTemplateName());
                i = templateMapper.insertTemplate(template);
                return i;
            }

            String vmFileNameString = template.getTemplateName() + "." + template.getFileType() + TemplateConstants.FILE_SUFFIX_VM;
            String dir = FileUtil.getWebRoot().getAbsolutePath() + "/" + TemplateConstants.SRC_MAIN_RESOURCES + "/" + parentPath;
            File vm = new File(dir);
            if (!FileUtil.exist(vm)) {
                vm = FileUtil.mkdir(dir);
            }
            if (FileUtil.file(vm.getPath() + "/" + vmFileNameString).exists()) {
                throw new IllegalArgumentException("文件存在");
            }

            // 创建resources文件和target文件
            File file = FileUtil.newFile(vm.getPath() + "/" + vmFileNameString);
            FileUtil.writeUtf8String(template.getContent(), file);
            File mkdir = FileUtil.mkdir(parentPath);
            File vmFile = FileUtil.newFile(mkdir + "/" + vmFileNameString);
            FileUtil.writeUtf8String(template.getContent(), vmFile);

            String fileName = file.getName();

            template.setFileName(fileName);
            template.setFileType(fileName.substring(fileName.indexOf('.') + 1, fileName.indexOf(".vm")));
            template.setPath(parentPath + "/" + vmFileNameString);
            i = templateMapper.insertTemplate(template);

        }catch (Exception e){
            e.printStackTrace();
            if ("".equals(template.getPath())) {
                FileUtil.del(FileUtil.file(template.getPath()).getParentFile());
                FileUtil.del(FileUtil.file(FileUtil.getWebRoot().getAbsolutePath() + "/src/main/resources/" + template.getPath()).getParentFile());
            }
        }

        return i;
    }

    /**
     * 修改模板信息
     *
     * @param template 模板信息
     * @return 结果
     */
    @Override
    public int updateTemplate(Template template) {
        template.setUpdateTime(DateUtil.date());
        int i = templateMapper.updateTemplate(template);
        if (i > 0){
            File file = FileUtil.file(template.getPath());
            File file1 = FileUtil.file(TemplateConstants.SRC_MAIN_RESOURCES_ABSOLUTE_PATH + template.getPath());
            FileUtil.writeUtf8String(template.getContent(),file1);
            FileUtil.writeUtf8String(template.getContent(),file);
        }
        return i;
    }

    /**
     * 批量删除模板信息
     *
     * @param templateIds 需要删除的模板信息主键
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteTemplateByTemplateIds(String[] templateIds) {
        int deleteCount = 0;
        try {
            Template template = templateMapper.selectTemplateByTemplateId(templateIds[0]);
            FileUtil.del(FileUtil.file(template.getPath()));
            FileUtil.del(FileUtil.file(FileUtil.getWebRoot().getAbsolutePath() + "/src/main/resources/" + template.getPath()));
            int i = templateMapper.deleteTemplateByTemplateIds(templateIds);
            if (i > 0 && TemplateConstants.TYPE_MENU.equals(template.getTemplateType())){
                Template tempTemplate = new Template();
                tempTemplate.setParentId(template.getTemplateId());
                List<Template> templates = templateMapper.selectTemplateList(tempTemplate);
                List<String> collect = templates.stream().filter(item -> !"".equals(item.getTemplateId())).map(Template::getTemplateId).collect(Collectors.toList());
                int i1 = templateMapper.deleteTemplateByTemplateIds(Convert.toStrArray(collect));
                deleteCount+=i+i1;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return deleteCount;
    }

    /**
     * 删除模板信息信息
     *
     * @param templateId 模板信息主键
     * @return 结果
     */
    @Override
    public int deleteTemplateByTemplateId(String templateId) {
        Template template = templateMapper.selectTemplateByTemplateId(templateId);
        FileUtil.del(FileUtil.file(template.getPath()).getParentFile());
        FileUtil.del(FileUtil.file(FileUtil.getWebRoot().getAbsolutePath() + "/src/main/resources/" + template.getPath()).getParentFile());
        return templateMapper.deleteTemplateByTemplateId(templateId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void importTemplate(List<Template> templates) {

        try {
            int i = importTemplateToFile(templates);
        } catch (Exception e) {
            e.printStackTrace();
            for (Template template : templates) {
                boolean del = FileUtil.del("vm/" + "/" + template.getFileName());
                boolean del2 = FileUtil.del(template.getPath());
                if (FileUtil.isDirEmpty(FileUtil.file("vm/" + template.getPath()).getParentFile())) {
                    boolean del1 = FileUtil.del(FileUtil.file("vm/" + template.getPath()).getParentFile());
                    boolean del3 = FileUtil.del(FileUtil.file(FileUtil.getWebRoot().getAbsolutePath() + "/src/main/resources/vm/" + template.getPath()).getParentFile());
                }
            }
        }

    }

    private int importTemplateToFile(List<Template> templates) {
        int updateNum = 0;
        for (Template template : templates) {

//            String dir = FileUtil.getWebRoot().getAbsolutePath()+"/src/main/resources/vm/";
//            File vm = new File(dir);
//            if (!FileUtil.exist(vm)) {
//                vm = FileUtil.mkdir(dir);
//            }
//            if (FileUtil.file(vm.getPath() + "/" + template.getTemplateName()).exists()){
//                continue;
//            }
//            File file = FileUtil.newFile(vm.getPath() + "/" + template.getTemplateName());
//            File utf8String = FileUtil.writeUtf8String(template.getContent(), file);
//            FileUtil.copy(vm,FileUtil.file("vm"),true);
//            String fileName = file.getName();
//            template.setFileName(fileName);
//            template.setFileType(fileName.substring(fileName.indexOf('.')+1,fileName.indexOf(".vm")));
//            int i = templateMapper.updateTemplate(template);
//            updateNum++;
        }
        return updateNum;
    }

    @Override
    public List<Template> selectTemplateListByIds(String[] ids) {
        return templateMapper.selectTemplateListByIds(ids);
    }

    @Override
    public List<Template> selectTemplateListByDynamic(Map<String, Object> map) {
//        map.put("groups",new String[]{"noobzz"});
//        map.put("ids",new String[]{"1","2"});
        List<Template> templates = templateMapper.selectTemplateListByDynamic(map);
        return templates;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int importTemplateByFile(MultipartFile file) {

        try {
            Template template = new Template();

            String content = IoUtil.readUtf8(file.getInputStream());
            String originalFilename = file.getOriginalFilename();

            template.setStatus("0");
            template.setParentId("2");
            template.setTemplateType("F");
            template.setCreateTime(DateUtil.date());
            template.setTemplateName(originalFilename);
            template.setContent(content);
            File newFile = FileUtil.newFile("vm" + "/" + template.getTemplateName());
            FileUtil.writeUtf8String(content, newFile);
            template.setFileName(originalFilename);
            template.setFileType(originalFilename.substring(originalFilename.indexOf('.') + 1, originalFilename.indexOf(".vm")));
            template.setPath(FileUtil.getCanonicalPath(newFile));
            int i = templateMapper.insertTemplate(template);

//            List<Template> templates = new ArrayList<>();
//            boolean add = templates.add(template);
//            importTemplateToFile(templates);

            return i;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }
}
