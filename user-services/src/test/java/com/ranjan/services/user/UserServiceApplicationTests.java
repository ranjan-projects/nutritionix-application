package com.ranjan.services.user;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
public class UserServiceApplicationTests {

    @MockBean
    private DiscoveryClient discoveryClient;

    @Test
    public void contextLoads(ApplicationContext context) {
        assertThat(context).isNotNull();

        Mockito.when(discoveryClient.getServices()).thenReturn(null);

        assertThat(discoveryClient.getServices()).isNull();
    }
}
