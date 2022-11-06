import cn.hutool.db.Db;
import cn.hutool.db.meta.JdbcType;
import cn.noobzz.gen.GenApplication;
import cn.noobzz.gen.domain.GenTable;
import cn.noobzz.gen.domain.GenTableColumn;
import cn.noobzz.gen.domain.Template;
import cn.noobzz.gen.service.GenTableColumnServiceImpl;
import cn.noobzz.gen.service.GenTableServiceImpl;
import cn.noobzz.gen.service.TemplateServiceImpl;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidPooledConnection;
import com.baomidou.dynamic.datasource.DynamicRoutingDataSource;
import com.baomidou.dynamic.datasource.toolkit.DynamicDataSourceContextHolder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author: ZZJ
 * @date: 2022/10/14
 * @desc:
 */
@SpringBootTest(classes = GenApplication.class)
public class MyTest {

    @Resource
    private DataSource dataSource;

    @Autowired
    private TemplateServiceImpl templateService;

    @Autowired
    private GenTableColumnServiceImpl genTableService;

    @Test
    public void getDatasource() throws ClassNotFoundException, SQLException {
        Connection root = DriverManager.getConnection("jdbc:mysql://localhost:3306/tt?serverTimezone=GMT%2B8" , "root" , "123456");
        DynamicRoutingDataSource ds = (DynamicRoutingDataSource) dataSource;
        DruidDataSource druidDataSource = new DruidDataSource();
        druidDataSource.setUrl("jdbc:mysql://localhost:3306/ry-cloud?serverTimezone=GMT%2B8");
        druidDataSource.setUsername("root");
        druidDataSource.setPassword("123456");
        druidDataSource.registerMbean();
        druidDataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        ds.addDataSource("localhost",druidDataSource);
        List<GenTableColumn> genTableColumns = genTableService.selectGenTableColumnListByTableId(24L);
        String localhost = DynamicDataSourceContextHolder.push("localhost");
//        boolean closed = root.isClosed();
//        DatabaseMetaData metaData = root.getMetaData();
//        System.out.println("schema = " + metaData);

//        Map<Object, Object> targetDataSources = new HashMap<>();
//        targetDataSources.put("druidDataSource",druidDataSource);
//        DynamicDataSource dynamicDataSource = new DynamicDataSource();
//        dynamicDataSource.setTargetDataSources(targetDataSources);
//        dynamicDataSource.setDefaultTargetDataSource(druidDataSource);
//        DataSourceContextHolder.setDataSources("druidDataSource");
        List<GenTableColumn> genTableColumns2 = genTableService.selectGenTableColumnListByTableId(2L);
        DynamicDataSourceContextHolder.clear();
        List<GenTableColumn> genTableColumns3 = genTableService.selectGenTableColumnListByTableId(24L);
        System.out.println("");
    }

    public static void main(String[] args) {

        String [] arr = {"东","南","西","北"};
        Stream<String> stream = Arrays.stream(arr);
        List<String> list = Arrays.asList("aa","cc","bb","aa","dd");
        List<String> collect = list.stream().sorted().collect(Collectors.toList());
        list.stream().sorted().forEach(System.out::println);
        list.stream().map(s -> s.length()).forEach(System.out::println);
        List<Integer> collect1 = list.stream().filter(s -> s.equals("aa")).map(s -> s.length()).collect(Collectors.toList());
        boolean aa = list.stream().anyMatch(s -> s.equals("aa"));
        System.out.println("aa = " + aa);
        List<Integer> integers = Arrays.asList(1, 2, 5, 3, 4, 7);
        integers.stream().filter(integer -> integer > 4).forEach(System.out::println);
    }
}
