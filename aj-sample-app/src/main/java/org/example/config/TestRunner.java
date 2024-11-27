package org.example.config;

import com.ajaxjs.springboot.BaseWebMvcConfigure;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * 在项目启动时定制化一些附加功能，比如：加载一些系统参数、完成初始化、预热本地缓存
 */
@Component
@Slf4j
public class TestRunner implements ApplicationRunner {
    @Override
    public void run(ApplicationArguments args) {
        String s = ("\n     ___       _       ___  __    __      _   _____        _          __  _____   _____  \n"
                + "     /   |     | |     /   | \\ \\  / /     | | /  ___/      | |        / / | ____| |  _  \\ \n"
                + "    / /| |     | |    / /| |  \\ \\/ /      | | | |___       | |  __   / /  | |__   | |_| |  \n"
                + "   / / | |  _  | |   / / | |   }  {    _  | | \\___  \\      | | /  | / /   |  __|  |  _  {  \n"
                + "  / /  | | | |_| |  / /  | |  / /\\ \\  | |_| |  ___| |      | |/   |/ /    | |___  | |_| |  \n"
                + " /_/   |_| \\_____/ /_/   |_| /_/  \\_\\ \\_____/ /_____/      |___/|___/     |_____| |_____/ \n");

        log.info(s);

        long elapsedTime = System.currentTimeMillis() - BaseWebMvcConfigure.APP_START_TIME;
        log.info("Spring App startup time: {} ms", elapsedTime);
    }
}