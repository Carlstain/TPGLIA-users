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
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.util.UriComponentsBuilder;
import series.users.models.SharedSerie;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class ShareControllerTest {
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
    public void getSharedScope(){
        assertThat(restTemplate.getForEntity("http://localhost:"+ port + "/share", SharedSerie[].class)
        .getStatusCode()).isIn(HttpStatus.OK, HttpStatus.NO_CONTENT);
        assertThat(restTemplate.getForEntity("http://localhost:"+ port + "/share/1", SharedSerie[].class)
                .getStatusCode()).isIn(HttpStatus.OK, HttpStatus.NO_CONTENT);
    }

    @Test
    public void ShareSeriescope() throws Exception {
        SharedSerie sharedSerie = new SharedSerie();
        sharedSerie.setSerieId((long) 1);
        sharedSerie.setUserId((long) 1);
        sharedSerie.setPermission("Read");

        MvcResult result = this.mockMvc.perform(post("/share")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJsonString(sharedSerie))).andReturn();
        assertThat(result.getResponse().getStatus()).isIn(200, 401, 403, 400);

    }

    @Test
    public void revokePermissionsScope() throws Exception {
        String url = "http://localhost:"+ port + "/share";
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("userid", 1)
                .queryParam("serieid", 1);
        MvcResult result = this.mockMvc.perform(delete("/share?userid=1&serieid=1")).andReturn();
        assertThat(result.getResponse().getStatus()).isIn(200, 401, 403, 400);
    }

    @Test
    public void privatizeScope() throws Exception {
        MvcResult result = this.mockMvc.perform(delete("/share/1")).andReturn();
        assertThat(result.getResponse().getStatus()).isIn(200, 401, 403);
    }
}
