package pl.dreilt.basicspringmvcapp;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

@SpringBootTest
class BasicSpringMvcAppApplicationTests {

    @Test
    void contextLoads() {
        Assert.isTrue(true, "Smoke test runs");
    }

}
