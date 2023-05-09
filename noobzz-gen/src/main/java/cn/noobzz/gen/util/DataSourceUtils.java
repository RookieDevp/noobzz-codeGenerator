package cn.noobzz.gen.util;

import cn.noobzz.gen.constant.DataSourceConstants;
import cn.noobzz.gen.domain.DataSource;
import com.baomidou.dynamic.datasource.toolkit.DynamicDataSourceContextHolder;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.function.Supplier;

/**
 * @author: ZZJ
 * @date: 2022/10/31
 * @desc:
 */
public class DataSourceUtils {

    public static boolean testConnection(DataSource source) {
        try {
            Connection connection = DriverManager.getConnection(source.getUrl(), source.getUsername(), source.getPassword());
            if (connection == null){
                return false;
            }
        }catch (Exception e){
            return false;
        }
        return true;
    }

    public static <T> T switchDatasource(String datasource, Supplier<T> statement) {
        DynamicDataSourceContextHolder.push(datasource);
        T result = statement.get();
        DynamicDataSourceContextHolder.push(DataSourceConstants.DATASOURCE_MASTER);
        return result;
    }
}
