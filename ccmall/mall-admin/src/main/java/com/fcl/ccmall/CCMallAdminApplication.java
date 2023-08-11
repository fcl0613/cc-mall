package com.fcl.ccmall;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
//@EnableTransactionManagement
public class CCMallAdminApplication {
    public static void main(String[] args) {
        SpringApplication.run(CCMallAdminApplication.class, args);
    }
}
