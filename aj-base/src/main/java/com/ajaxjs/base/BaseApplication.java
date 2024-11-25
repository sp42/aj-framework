package com.ajaxjs.base;

import com.ajaxjs.embeded_tomcat.EmbeddedTomcatStarter;
import com.ajaxjs.framework.BaseWebMvcConfigure;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableWebMvc
@ComponentScan({"com.ajaxjs.base"})
public class BaseApplication extends BaseWebMvcConfigure {
    public static void main(String[] args) {
        EmbeddedTomcatStarter.start(BaseApplication.class);
    }
}
