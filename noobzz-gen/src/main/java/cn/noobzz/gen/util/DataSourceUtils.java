package cn.noobzz.gen.util;

import cn.noobzz.gen.domain.DataSource;

import java.sql.Connection;
import java.sql.DriverManager;

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
}
