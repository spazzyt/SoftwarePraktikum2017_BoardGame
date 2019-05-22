package ch.uzh.ifi.seal.soprafs17;

import ch.uzh.ifi.seal.soprafs17.bot.AIBot;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

/**
 * Created by nairboon on 10.05.17.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)

public class AIBotTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void someFailing() throws IOException {

        RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder()
                .rootUri("http://localhost:" + 123);

      //  restTemplate = restTemplateBuilder.build();

        AIBot frodo = new AIBot(restTemplate.getRestTemplate(), 12);

        Assert.assertTrue(frodo.createUserAccount("abc"));
        Assert.assertTrue(frodo.createGame());
        frodo.user.setToken("abbbbb");
        Assert.assertFalse(frodo.createGame());

    }
}
