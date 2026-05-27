package com.manpowergroup.kintai.starter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.manpowergroup.kintai")
public class KintaiStarterApplication {

    public static void main(String[] args) {
        SpringApplication.run(KintaiStarterApplication.class, args);
    }

}
