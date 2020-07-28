package org.iii.converter.config;

import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.iii.converter.ConverterUiApplication;
import org.iii.converter.exception.DBInitErrorException;
import org.iii.converter.utility.DBInitialzr;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
@Slf4j
public class DataSourceConfig {
    @Bean
    public DataSource getDataSource() {
        try {
            DBInitialzr.DBConfig config = DBInitialzr.initDB();
            DataSourceBuilder<HikariDataSource> builder = DataSourceBuilder.create().type(HikariDataSource.class);
            builder.driverClassName("org.h2.Driver");
            builder.url(config.getConnStr());
            builder.username(config.getUser());
            builder.password(config.getPassword());
            return builder.build();
        } catch (DBInitErrorException ex) {
            log.error(ExceptionUtils.getStackTrace(ex));
            System.exit(1);
            return null;
        }
    }
}
