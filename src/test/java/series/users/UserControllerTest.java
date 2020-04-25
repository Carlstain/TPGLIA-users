package series.users;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import org.springframework.test.web.servlet.MvcResult;
import series.users.models.User;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class UserControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TestRestTemplate restTemplate;

    public static String toJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void registerScope() throws Exception {
        User user = new User((long) 0, "testUser", "testPassword", "User");

        MvcResult result = this.mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJsonString(user))).andReturn();
        assertThat(result.getResponse().getStatus()).isIn(201, 400);
    }
    @Test
    public void shouldGetauthicatedUser(){
        assertThat(restTemplate.getForObject("http://localhost:" + port + "/authenticate", User.class)).isNotNull();
    }

    @Test
    public void getUsersShouldReturnOkorNoCotent (){
        assertThat(restTemplate.getForEntity("http://localhost:"+ port + "/users", User[].class)
                    .getStatusCode()).isIn(HttpStatus.OK, HttpStatus.NO_CONTENT);
        assertThat(restTemplate.getForEntity("http://localhost:"+ port + "/users/1", User[].class)
                .getStatusCode()).isIn(HttpStatus.OK, HttpStatus.NO_CONTENT);
        assertThat(restTemplate.getForEntity("http://localhost:"+ port + "/users?name=username", User[].class)
                .getStatusCode()).isIn(HttpStatus.OK, HttpStatus.NO_CONTENT);
    }
}
