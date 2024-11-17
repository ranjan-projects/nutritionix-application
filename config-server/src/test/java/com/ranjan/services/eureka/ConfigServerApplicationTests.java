package com.ranjan.services.eureka;

import com.ranjan.services.configserver.ConfigServerApplication;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest(classes = ConfigServerApplication.class)
@TestPropertySource(locations = "classpath:application-test.properties")
class ConfigServerApplicationTests {

    @Test
    void contextLoads() {
    }

}
