package cn.noobzz.gen;

import com.github.pagehelper.autoconfigure.PageHelperAutoConfiguration;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.annotation.MapperScans;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * @author: ZZJ
 * @date: 2022/09/03
 * @desc:
 */
@SpringBootApplication(exclude = {PageHelperAutoConfiguration.class})
public class GenApplication {

    public static void main(String[] args) {
        SpringApplication.run(GenApplication.class,args);
    }
}
