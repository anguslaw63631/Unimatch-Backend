package com.unimatch.unimatch_backend;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import java.net.InetAddress;
import java.net.UnknownHostException;


@SpringBootApplication
@Slf4j
@EnableSwagger2
@EnableKnife4j
public class UnimatchBackendApplication {

    public static void main(String[] args) throws UnknownHostException {
        ConfigurableApplicationContext application = SpringApplication.run(UnimatchBackendApplication.class, args);
        //print application info
        info(application);
    }

    static void info(ConfigurableApplicationContext application) throws UnknownHostException {
        Environment env = application.getEnvironment();
        String ip = InetAddress.getLocalHost().getHostAddress();
        String port = env.getProperty("server.port");
        String contextPath = env.getProperty("server.servlet.context-path");
        if (contextPath == null) {
            contextPath = "";
        }
        log.info("\n====================UniMatch Backend Server====================\n" +
                "API path: \thttp://" + ip + ':' + port + contextPath + '\n' +
                "API Doc: \thttp://" + ip + ":" + port + contextPath + "/doc.html\n" +
                "===============================================================");
    }
}
