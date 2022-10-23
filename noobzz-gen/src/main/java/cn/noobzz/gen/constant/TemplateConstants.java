package cn.noobzz.gen.constant;

import cn.hutool.core.io.FileUtil;

/**
 * @author: ZZJ
 * @date: 2022/10/13
 * @desc: 模板类常量
 */
public class TemplateConstants {

    public static final String TYPE_MENU = "M";

    public static final String TYPE_FILE = "F";

    public static final String FILE_JAVA = "java";

    public static final String FILE_VUE = "vue";

    public static final String FILE_SQL = "sql";

    public static final String FILE_XML = "xml";

    public static final String FILE_JS = "js";

    public static final String STATUS_OK = "0";

    public static final String STATUS_OFF = "1";

    public static final String TEMPLATE_VM = "vm";

    public static final String FILE_SUFFIX_VM = ".vm";

    public static final String TEMPLATE_DEFAULT = "default";

    public static final String SRC_MAIN_RESOURCES = "src/main/resources";

    public static final String SRC_MAIN_RESOURCES_ABSOLUTE_PATH = FileUtil.getWebRoot().getAbsolutePath() + "/src/main/resources/";
}
