package series.users;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import series.users.controller.Controller;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class UsersApplicationTests {
    @Autowired
    private Controller controller;

    @Test
    void contextLoads() {
        assertThat(controller).isNotNull();
    }

}
