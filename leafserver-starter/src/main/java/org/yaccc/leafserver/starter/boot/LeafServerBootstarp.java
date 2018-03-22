package org.yaccc.leafserver.starter.boot;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

/**
 * Created by xiezhaodong  on 2018/2/11
 */
@Slf4j
@SpringBootApplication(scanBasePackages = {"org.yaccc.leafserver.*"})
@MapperScan("org.yaccc.leafserver.persistent")
public class LeafServerBootstarp {

    @Bean(name = "txManager")
    public PlatformTransactionManager txManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }



    public static void main(String[] args) {
        try {
            log.info("Initializing leafserver...");
            SpringApplication.run(LeafServerBootstarp.class, args);
            log.info("Started leafserver , enjoy it!!");


        } catch (Exception e) {
            log.error("start leafserver error {}", e);
            System.exit(0);
        }
    }

}
