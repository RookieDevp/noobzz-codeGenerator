package cn.noobzz.gen.domain;

import lombok.Data;
import lombok.Getter;

/**
 * @author ZZJ
 * @date 2024/01/06
 * @desc
 */
@Data
@Getter
public class GenConfig {

    private Long id;

    /** 作者 */
    private String author;

    /** 生成包路径 */
    private String packageName;

    /** 自动去除表前缀，默认是false */
    private boolean autoRemovePre;

    /** 表前缀(类名不会包含表前缀) */
    private String tablePrefix;
}
