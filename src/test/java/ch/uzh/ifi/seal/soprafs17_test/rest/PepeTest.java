package ch.uzh.ifi.seal.soprafs17.rest;

import ch.uzh.ifi.seal.soprafs17.Application;
import ch.uzh.ifi.seal.soprafs17.entity.User;
import ch.uzh.ifi.seal.soprafs17.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)

public class PepeTest {

    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));

    private User testUser;

    private boolean enabled = true;

    @Autowired
    private UserService userService;


    HttpHeaders headers = new HttpHeaders();

    @Autowired
    private TestRestTemplate restTemplate;

    private HttpEntity<String> makeEntity(String obj) {
        return new HttpEntity<String>(obj, headers);
    }

    public PepeTest() {
        headers.setContentType(MediaType.APPLICATION_JSON);
    }

    @Before
    public void setup() throws Exception {

        testUser = userService.createUser("Brudi42^2");

    }

    @Test
    public void pepeIsLife() throws Exception {

        Map<String,String> urlParam = new HashMap<>();
        urlParam.put("token", this.testUser.getToken());

        ObjectMapper mapper = new ObjectMapper();

        // because websockets fail on jenkins
        assertTrue(true);

if( enabled) {
    ResponseEntity<String> response = restTemplate.postForEntity("/pepe?token={token}", makeEntity(""), String.class, urlParam);
    String body = response.getBody();
    MediaType contentType = response.getHeaders().getContentType();
    HttpStatus statusCode = response.getStatusCode();

    assertEquals(HttpStatus.CREATED, statusCode);

    // the game id
    assertEquals("1", body);

}

      //  if(!statusCode.is2xxSuccessful())
      /*  this.mockMvc.perform(post("/pepe" )
                .param("token", testUser.getToken())
                .contentType(contentType))
                .andExpect(status().isCreated());*/
    }

    @Test
    public void bobIsLife() throws Exception {

        Map<String,String> urlParam = new HashMap<>();
        urlParam.put("token", this.testUser.getToken());

        ObjectMapper mapper = new ObjectMapper();

        // because websockets fail on jenkins
        assertTrue(true);

        if( enabled) {
            ResponseEntity<String> response = restTemplate.postForEntity("/bob?token={token}", makeEntity(""), String.class, urlParam);
            String body = response.getBody();
            MediaType contentType = response.getHeaders().getContentType();
            HttpStatus statusCode = response.getStatusCode();

            assertEquals(HttpStatus.CREATED, statusCode);

            // the game id
            assertEquals("1", body);

        }

        //  if(!statusCode.is2xxSuccessful())
      /*  this.mockMvc.perform(post("/pepe" )
                .param("token", testUser.getToken())
                .contentType(contentType))
                .andExpect(status().isCreated());*/
    }

}
