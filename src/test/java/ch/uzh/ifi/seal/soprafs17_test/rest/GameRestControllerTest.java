
package ch.uzh.ifi.seal.soprafs17.rest;

import ch.uzh.ifi.seal.soprafs17.Application;
import ch.uzh.ifi.seal.soprafs17.entity.Game;
import ch.uzh.ifi.seal.soprafs17.entity.Move;
import ch.uzh.ifi.seal.soprafs17.entity.User;
import ch.uzh.ifi.seal.soprafs17.repository.GameRepository;
import ch.uzh.ifi.seal.soprafs17.repository.UserRepository;
import ch.uzh.ifi.seal.soprafs17.service.GameService;
import ch.uzh.ifi.seal.soprafs17.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

/*

setConverters(HttpMessageConverter<?>[] converters)
void setup() throws Exception
void createGame() throws Exception
void getGame() throws Exception
void joinGame() throws Exception
void leaveGame() throws Exception
String json(Object o) throws IOException

 */

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class GameRestControllerTest {

    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));

    private MockMvc mockMvc;

    private HttpMessageConverter mappingJackson2HttpMessageConverter;

    private User testUser;

    private Game testGame;

    //private ArrayList<Game> gameList = new ArrayList<>();

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private GameService gameService;

    @Autowired
    private WebApplicationContext webApplicationContext;

    //@Autowired should only be used here in the setter, probably not everywhere
    @Autowired
    void setConverters(HttpMessageConverter<?>[] converters) {

        this.mappingJackson2HttpMessageConverter = Arrays.asList(converters).stream()
                .filter(hmc -> hmc instanceof MappingJackson2HttpMessageConverter)
                .findAny()
                .orElse(null);

        assertNotNull("the JSON message converter must not be null",
                this.mappingJackson2HttpMessageConverter);
    }

    @Before
    public void setup() throws Exception {
        this.mockMvc = webAppContextSetup(webApplicationContext).build();


        testUser = userService.createUser("Brudi8");
        testGame = gameService.createGame(testUser.getToken(),3);
    }


    @Test
    public void createGame() throws Exception {

        Game newGame = new Game();
        newGame.setName("String: newGame von GameRestControllerTest.");
        newGame.setNumPlayers(2);


        String gameJson = json(newGame);

        this.mockMvc.perform(post("/game")
                .param("token", testUser.getToken())
                .contentType(contentType)
                .content(gameJson))
                .andExpect(status().isCreated())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.numPlayers", is(2)))
                .andExpect(jsonPath("$.players", hasSize(1)))
                .andExpect(jsonPath("$.players[0].token", is(testUser.getToken())));


    }



    @Test
    public void getGame() throws Exception {


        this.mockMvc.perform(get("/game/"+ testGame.getId())
                .param("token", testUser.getToken())
                .contentType(contentType))
               // .andExpect(content().)
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.numPlayers", is(3)))
                .andExpect(jsonPath("$.players", hasSize(1)))
                .andExpect(jsonPath("$.players[0].token", is(testUser.getToken())));


        this.mockMvc.perform(get("/game/"+ "9999")
                .param("token", testUser.getToken())
                .contentType(contentType))
                .andExpect(status().isNotFound());
    }


    @Test
    public void getGameList() throws Exception {


        this.mockMvc.perform(get("/game/")
                .param("token", testUser.getToken())
                .contentType(contentType))
                // .andExpect(content().)
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"));
    }



    @Test
    public void joinGame() throws Exception {

        User testUser = userService.createUser("Brudi9");

        //standard case
        this.mockMvc.perform(post("/game/" + testGame.getId() + "/join" )
                .param("token", testUser.getToken())
                .contentType(contentType))
                .andExpect(status().isOk());

        //testGame is not found
        this.mockMvc.perform(post("/game/" + -1 + "/join" )
                .param("token", testUser.getToken())
                .contentType(contentType))
                .andExpect(status().isNotFound());

        //user token deviant
        this.mockMvc.perform(post("/game/" + testGame.getId() + "/join" )
                .param("token", "Hacker")
                .contentType(contentType))
                .andExpect(status().isForbidden());

        //testGame not found && user token deviant
        this.mockMvc.perform(post("/game/" + -1 + "/join" )
                .param("token", "")
                .contentType(contentType))
                .andExpect(status().isNotFound());
    }





    @Test
    public void getDecisionForPlayer() throws Exception{

        this.mockMvc.perform(get("/game/" + "99999"+ "/actions" )
                .param("token", testUser.getToken())
                .contentType(contentType))
                .andExpect(status().isNotFound());


        gameService.startGame(testGame);

        this.mockMvc.perform(get("/game/" + testGame.getId() + "/actions" )
                .param("token", testUser.getToken())
                .contentType(contentType))
                .andExpect(status().isNotFound());
    }



    //


    @Test
    public void addMove() throws Exception {

        Move m = new Move("Cyka","Blyat",42);
        String mJson = json(m);

        this.mockMvc.perform(post("/game/" + 9999 + "/move" )
                .param("token", testUser.getToken())
                .contentType(contentType)
                .content(mJson))
                .andExpect(status().isNotFound());

        this.mockMvc.perform(post("/game/" + testGame.getId() + "/move" )
                .param("token", "hacker")
                .contentType(contentType)
                .content(mJson))
                .andExpect(status().isForbidden());

        testGame.setUpGame();
        gameService.startGame(testGame);


        this.mockMvc.perform(post("/game/" + testGame.getId() + "/move" )
                .param("token", testUser.getToken())
                .contentType(contentType)
                .content(mJson))
                .andExpect(status().isInternalServerError());
    }


    @Test
    public void leaveGame() throws Exception {
        this.mockMvc.perform(post("/game/" + testGame.getId() + "/leave" )
                .param("token", testUser.getToken())
                .contentType(contentType))
                .andExpect(status().isOk());


        this.mockMvc.perform(post("/game/" + testGame.getId() + "/leave" )
                .param("token", "abcd")
                .contentType(contentType))
                .andExpect(status().isNotFound());
    }





    protected String json(Object o) throws IOException {
        MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
        this.mappingJackson2HttpMessageConverter.write(
                o, MediaType.APPLICATION_JSON, mockHttpOutputMessage);
        return mockHttpOutputMessage.getBodyAsString();
    }





}
