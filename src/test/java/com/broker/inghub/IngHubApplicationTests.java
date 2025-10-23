package com.broker.inghub;

import com.broker.inghub.controller.OrderController;
import com.broker.inghub.service.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class IngHubApplicationTests {

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    void contextLoads_andCoreBeansPresent() {
        assertThat(applicationContext).isNotNull();
        assertThat(applicationContext.getBean(OrderController.class)).isNotNull();
        assertThat(applicationContext.getBean(OrderService.class)).isNotNull();
    }
}
