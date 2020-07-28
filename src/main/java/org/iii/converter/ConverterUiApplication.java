package org.iii.converter;

import java.util.Properties;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import org.iii.converter.exception.DBInitErrorException;
import org.iii.converter.utility.DBInitialzr;

@SpringBootApplication
@Controller
@Slf4j
public class ConverterUiApplication {

    public static void main(String[] args) {
        try {
            DBInitialzr.DBConfig config = DBInitialzr.initDB();
        } catch (DBInitErrorException ex) {
            log.error(ExceptionUtils.getStackTrace(ex));
            System.exit(1);
        }

        SpringApplication.run(ConverterUiApplication.class, args);
    }

    @GetMapping("/exit")
    public void exit() {
        log.info("converter is exiting...");
        System.exit(0);
    }

    @GetMapping("/index")
    public String getIndex() {
        return "index";
    }

    @GetMapping("/provider")
    public String getProviderPage() {
        return "provider";
    }

    @GetMapping("/datasets")
    public String getDatasetsPage() {
        return "datasets";
    }
}
