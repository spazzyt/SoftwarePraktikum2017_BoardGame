package ch.uzh.ifi.seal.soprafs17.bot;

import ch.uzh.ifi.seal.soprafs17.gamelogic.Decision;
import ch.uzh.ifi.seal.soprafs17.bot.strategies.BotStrategy;
import ch.uzh.ifi.seal.soprafs17.bot.strategies.FirstMoveStrategy;
import ch.uzh.ifi.seal.soprafs17.constant.GameStatus;
import ch.uzh.ifi.seal.soprafs17.entity.Game;
import ch.uzh.ifi.seal.soprafs17.entity.Move;
import ch.uzh.ifi.seal.soprafs17.entity.User;
import ch.uzh.ifi.seal.soprafs17.web.websockets.messages.WSMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.socket.client.WebSocketConnectionManager;
import org.springframework.web.socket.client.jetty.JettyWebSocketClient;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import static java.util.concurrent.TimeUnit.SECONDS;

public class AIBot {

    private final Logger log = LoggerFactory.getLogger(AIBot.class);


    private int moveTimeout;

    private int port;

    public User user;

    public Game game;

    public SimpleClientWebSocketHandler handler;

    private boolean autoPlay = true;

    CompletableFuture ended = null;

    private HttpHeaders headers = new HttpHeaders();

   // @Autowired
    private RestTemplate restTemplate;


    public BotStrategy strategy;

    Decision myDecision;

    public AIBot(RestTemplate rt, int p) {
        headers.setContentType(MediaType.APPLICATION_JSON);
        restTemplate = rt;
        port = p;

        moveTimeout = 500;
        strategy = new FirstMoveStrategy();
    }


    private HttpEntity<String> makeEntity(String obj) {
        return new HttpEntity<>(obj, headers);
    }



    public Long getGameId() {
        return this.game.getId();
    }

    public boolean createUserAccount(String myUsername) throws IOException {


        User newUser = new User();
        newUser.setUsername(myUsername);
        newUser.isRarePepe=true;
        ObjectMapper mapper = new ObjectMapper();


        ResponseEntity<String> response = restTemplate.postForEntity("/user", makeEntity(mapper.writeValueAsString(newUser)), String.class);
        String body = response.getBody();
       /* HttpStatus statusCode = response.getStatusCode();


       if(!statusCode.is2xxSuccessful())
            return false;
*/

        // back to obj
        this.user = mapper.readValue(body, User.class);

        // check that we got our username
      //  if(!myUsername.equals(this.user.getUsername()))
      //      return false;


        return true;
    }

    public boolean createGame() throws IOException {

        Game g = new Game();
        g.setNumPlayers(2);

        Map<String,String> urlParam = new HashMap<>();
        urlParam.put("token", this.user.getToken());

        ObjectMapper mapper = new ObjectMapper();

        ResponseEntity<String> response = restTemplate.postForEntity("/game?token={token}", makeEntity(mapper.writeValueAsString(g)), String.class, urlParam);
        String body = response.getBody();
        HttpStatus statusCode = response.getStatusCode();

        if(!statusCode.is2xxSuccessful())
            return false;

        // back to obj

        this.game = mapper.readValue(body, Game.class);

        /*

        // check that we got our username
        if(!this.user.getUsername().equals( this.game.getPlayers().get(0).getUsername()))
            return false;


        // still pending
        if(GameStatus.PENDING != this.game.getStatus())
            return false;

        */
        return true;
    }


    public boolean joinGame(Long id) throws IOException {

        Map<String,String> urlParam = new HashMap<>();
        urlParam.put("token", this.user.getToken());
        urlParam.put("id", id.toString());

        ResponseEntity<String> response = restTemplate.postForEntity("/game/{id}/join?token={token}", "", String.class, urlParam);
        String body = response.getBody();
       // HttpStatus statusCode = response.getStatusCode();

       /* if(!statusCode.is2xxSuccessful())
            return false;
        */

        // back to obj
        ObjectMapper mapper = new ObjectMapper();
        this.game = mapper.readValue(body, Game.class);

        /*
        // check that we got our username now as player 2
        if(!this.user.getUsername().equals( this.game.getPlayers().get(1).getUsername()))
            return false;
        // should be starting now
        if(GameStatus.STARTING != this.game.getStatus())
            return false;
        */

        return true;
    }

    public boolean joinWebsocket(CompletableFuture future) throws InterruptedException, ExecutionException, TimeoutException {

        CompletableFuture<String> futureC = new CompletableFuture<>();

        this.handler = new SimpleClientWebSocketHandler(futureC,  this);
        WebSocketConnectionManager manager = new WebSocketConnectionManager(new JettyWebSocketClient(),
                this.handler, "ws://localhost:" + port + "/websocket");


        manager.start();
       /* if(!futureC.get(60, SECONDS).equals("CONNECTED"))
            return false;
        */
        futureC.get(60, SECONDS);

        future.complete(null);

        return true;
    }

    public boolean itsMyTurn(CompletableFuture future) {

        WSMessage last = handler.getMessages().get(handler.getMessages().size()-1);


        HashMap<String,String> mm = (HashMap<String, String>) last.payload;
        String who = mm.get("user");

        return (last.type == WSMessage.MessageType.CURRENTTURN && who.equals(user.getUsername()));
    }

    public Decision getAvailableDecision(CompletableFuture future) {

        log.debug("bot:getAvDecision: ");

        Map<String,String> urlParam = new HashMap<>();
        urlParam.put("token", this.user.getToken());
        urlParam.put("id", game.getId().toString());

        ResponseEntity<Decision> response = restTemplate.getForEntity("/game/{id}/actions?token={token}", Decision.class, urlParam);
        Decision body = response.getBody();
        //HttpStatus statusCode = response.getStatusCode();

       /* if(!statusCode.is2xxSuccessful())
            return null; */

        this.myDecision = body;

        future.complete(null);
        return body;
    }


    public boolean makeMove(Move m, CompletableFuture future) throws IOException, InterruptedException {

        log.debug("bot:makeMove: ", m);

        Thread.sleep(moveTimeout);
        Map<String,String> urlParam = new HashMap<>();
        urlParam.put("token", this.user.getToken());
        urlParam.put("id", getGameId().toString());

        ObjectMapper mapper = new ObjectMapper();

        ResponseEntity<Decision> response = restTemplate.postForEntity("/game/{id}/move?token={token}", makeEntity(mapper.writeValueAsString(m)), Decision.class, urlParam);
        Decision body = response.getBody();
        HttpStatus statusCode = response.getStatusCode();

        if(!statusCode.is2xxSuccessful())
            return false;

        this.myDecision = body;

        future.complete(null);
        return true;
    }

    public void setEnded(CompletableFuture ended) {
        this.ended = ended;
    }


    public void setAutoPlay(boolean autoPlay) {
        this.autoPlay = autoPlay;
    }

    boolean isAutoPlay() {
        return autoPlay;
    }

    public void setMoveTimeout(int moveTimeout) {
        this.moveTimeout = moveTimeout;
    }
}