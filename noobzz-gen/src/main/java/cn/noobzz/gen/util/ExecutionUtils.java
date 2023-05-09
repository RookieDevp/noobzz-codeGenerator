package cn.noobzz.gen.util;

import cn.noobzz.gen.constant.DataSourceConstants;
import com.baomidou.dynamic.datasource.toolkit.DynamicDataSourceContextHolder;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * @author: ZZJ
 * @date: 2023/05/04
 * @desc:
 */
public class ExecutionUtils {

    public static <T> T executeWithPreAndPostActions(String datasource,Supplier<T> statement) {
        DynamicDataSourceContextHolder.push(datasource);
        T result = statement.get();
        DynamicDataSourceContextHolder.push(DataSourceConstants.DATASOURCE_MASTER);
        return result;
    }

    public static <T> void executeWithPreAndPostActions(Supplier<T> preAction, Supplier<T> statement, Consumer<T> postAction) {
        T result = preAction.get();
        try {
            result = statement.get();
        } finally {
            postAction.accept(result);
        }
    }
}
