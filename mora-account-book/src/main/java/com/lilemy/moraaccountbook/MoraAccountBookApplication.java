package com.lilemy.moraaccountbook;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Slf4j
@SpringBootApplication
@EnableAspectJAutoProxy(exposeProxy = true)
@MapperScan("com.lilemy.moraaccountbook.mapper")
public class MoraAccountBookApplication {

    public static void main(String[] args) {
        SpringApplication.run(MoraAccountBookApplication.class, args);
        log.info("(♥◠‿◠)ﾉﾞ  摩拉记账本启动成功   ლ(´ڡ`ლ)ﾞ");
    }

}
