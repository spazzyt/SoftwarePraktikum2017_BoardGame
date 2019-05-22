package ch.uzh.ifi.seal.soprafs17;

import ch.uzh.ifi.seal.soprafs17.gamelogic.Decision;
import ch.uzh.ifi.seal.soprafs17.bot.strategies.CardLoverStrategy;
import ch.uzh.ifi.seal.soprafs17.bot.strategies.RandomDudeStrategy;
import ch.uzh.ifi.seal.soprafs17.entity.Game;
import ch.uzh.ifi.seal.soprafs17.entity.Move;
import ch.uzh.ifi.seal.soprafs17.service.GameService;
import ch.uzh.ifi.seal.soprafs17.bot.AIBot;
import ch.uzh.ifi.seal.soprafs17.web.websockets.messages.WSMessage;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static java.util.Arrays.asList;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AIGameTest {

    static Logger logger  = LoggerFactory.getLogger(AIGameTest.class);

    @Value("${local.server.port}")
    private int port;

    private HttpMessageConverter mappingJackson2HttpMessageConverter;

    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));


    private boolean enabled = true;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private GameService gameService;

    public AIGameTest() {

    }

    /*
    @Test
    public void connectWS() throws Exception {

        CompletableFuture<String> future = new CompletableFuture<>();

        SimpleClientWebSocketHandler handler = new SimpleClientWebSocketHandler(future);
        WebSocketConnectionManager manager = new WebSocketConnectionManager(new StandardWebSocketClient(),
                handler, "ws://localhost:" + port + "/websocket");


        manager.start();

        assertEquals("CONNECTED", future.get(2, SECONDS));

        /*
        port=8080;
        logger.debug("yoooooo: " + port);


        StompSession session = stompClient
                .connect("ws://localhost:" + port + "/websocket", new StompSessionHandlerAdapter() {
                })
                .get(2, SECONDS);

*/

       // stompClient = new WebSocketStompClient(new SockJsClient(
       //         asList(new WebSocketTransport(new StandardWebSocketClient()))));

        //stompClient.setMessageConverter(new MappingJackson2MessageConverter());
/*
        List<Transport> transports = new ArrayList<>(2);
        transports.add(new WebSocketTransport(new StandardWebSocketClient()));
        transports.add(new RestTemplateXhrTransport());

        SockJsClient sockJsClient = new SockJsClient(transports);

        WebSocketClient webSocketClient = new StandardWebSocketClient();
        WebSocketStompClient stompClient = new WebSocketStompClient(sockJsClient);
        stompClient.setMessageConverter(new StringMessageConverter());


        //port=8080;

        CompletableFuture<String> future = new CompletableFuture<>();
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.submit(new Runnable() {
            @Override
            public void run() {

                StompSession session = null;
                try {
                    session = stompClient
                            .connect("ws://localhost:" + port + "/websocket", new StompSessionHandlerAdapter() {})
                            .get(2, SECONDS);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (TimeoutException e) {
                    e.printStackTrace();
                }

                Assert.assertTrue(session.isConnected());


                 future.complete("Hello World!2");
            }
        });

        assertEquals("Hello World!", future.get(2, TimeUnit.SECONDS));
*

    }
*/



    @Test
    public void theAIGame() throws Exception {

        AIBot a = new AIBot(restTemplate.getRestTemplate(), port);
        assertTrue(a.createUserAccount("Brudi8(aka kenny)"));

        AIBot b = new AIBot(restTemplate.getRestTemplate(),port);
        assertTrue(b.createUserAccount("the coder"));


        a.setMoveTimeout(30);
        b.setMoveTimeout(30);

        a.setAutoPlay(false);
        b.setAutoPlay(false);

        assertTrue(a.createGame());
        assertTrue(b.joinGame(a.getGameId()));

        TimeUnit.MILLISECONDS.sleep(150);


        CompletableFuture future = new CompletableFuture<>();

        // disable on jenkins
        if(enabled) {
            // connect the first player to the websocket
            assertTrue(a.joinWebsocket(future));
            TimeUnit.MILLISECONDS.sleep(500);

            Assert.assertNull(future.get(220, SECONDS));


            TimeUnit.MILLISECONDS.sleep(600);
            Assert.assertNotNull(a.handler);
            assertEquals(2, a.handler.getMessages().size());
            assertEquals(WSMessage.MessageType.OK, a.handler.getMessages().get(0).type);
            assertEquals(WSMessage.MessageType.USERCONNECTED, a.handler.getMessages().get(1).type);

            future = new CompletableFuture<>();
            b.joinWebsocket(future);
            Assert.assertNull(future.get(60, SECONDS));

            TimeUnit.MILLISECONDS.sleep(900);

            assertEquals(5, a.handler.getMessages().size());

        /*
         by now the game has started, thus a should have the following messages:
            OK, USERCONNECTED, USERCONNECTED, NEWROUND, CURRENTTURN
         */

            // other player
            assertEquals(WSMessage.MessageType.USERCONNECTED, a.handler.getMessages().get(2).type);

            TimeUnit.MILLISECONDS.sleep(200);

            // new round
            assertEquals(WSMessage.MessageType.NEWROUND, a.handler.getMessages().get(3).type);
            // currenturn
            assertEquals(WSMessage.MessageType.CURRENTTURN, a.handler.getMessages().get(4).type);

            TimeUnit.MILLISECONDS.sleep(200);


            assertEquals(4, b.handler.getMessages().size());

            assertEquals(WSMessage.MessageType.USERCONNECTED, b.handler.getMessages().get(1).type);
            assertEquals(WSMessage.MessageType.NEWROUND, b.handler.getMessages().get(2).type);


            TimeUnit.MILLISECONDS.sleep(200);
            assertTrue(a.itsMyTurn(future));
            Assert.assertFalse(b.itsMyTurn(future));


            // direct access
            Game theGame = gameService.getRunningGame(a.getGameId());
            Assert.assertNotNull(theGame);
            assertEquals(2, theGame.getPlayers().size());
            Assert.assertNotNull(theGame.getPlayer(a.user.getToken()));
            Assert.assertNotNull(theGame.getPlayer(a.user.getToken()).decision);

            // now only quarry to sled + sled to ship
            final int availableActionsAtStart = 2;
            assertEquals(availableActionsAtStart, theGame.getPlayer(a.user.getToken()).decision.actions.size());

            TimeUnit.MILLISECONDS.sleep(200);
            Decision dFora = a.getAvailableDecision(future);

            Assert.assertNotNull(dFora);

            // right now only stone placement
            assertEquals(availableActionsAtStart, dFora.actions.size());
            assertTrue(dFora.actions.get(0).moves.size() > 0);

            // now execute that thing
            Move m = dFora.actions.get(0).moves.get(0);
            assertNotNull(m);

            TimeUnit.MILLISECONDS.sleep(200);
            future = new CompletableFuture<>();
            a.makeMove(m, future);
            Assert.assertNull(future.get(2, SECONDS));


            TimeUnit.MILLISECONDS.sleep(800);
            // old size: 5 + move + nexturn = 7 + irgendwas
            assertEquals(7, a.handler.getMessages().size());
            assertEquals(WSMessage.MessageType.PLAYEDMOVE, a.handler.getMessages().get(5).type);
            assertEquals(WSMessage.MessageType.CURRENTTURN, a.handler.getMessages().get(6).type);
            //assertEquals(WSMessage.payload, a.handler.getMessages().get(5).type);

            TimeUnit.MILLISECONDS.sleep(100);

            Assert.assertFalse(a.itsMyTurn(future));
            assertTrue(b.itsMyTurn(future));

            for (int i = 0; i < 3; i++) {
                AIBot active = null;

                if (b.itsMyTurn(null)) {
                    active = b;
                } else if (a.itsMyTurn(null)) {
                    active = a;
                } else {
                    Assert.assertNotNull(" \\:/ what");
                    break;
                }
                TimeUnit.MILLISECONDS.sleep(150);

                assertNotNull(active);
                Decision myDecision = active.getAvailableDecision(future);
                Assert.assertNull(future.get(1, SECONDS));
                if (myDecision == null) {
                    Assert.assertNotNull(myDecision);
                }
                future = new CompletableFuture<>();

                //myDecision.actions.
                int pickC = 1;
                int pickM = 1;

                assertTrue(myDecision.actions.size() > 0);

                if(myDecision.actions.size() == 1)
                    pickC = 0;

                Assert.assertNotNull(myDecision.actions.get(pickC));
                int avMoves = myDecision.actions.get(pickC).moves.size();
                assertTrue( avMoves > 0);

                if( avMoves == 1)
                    pickM = 0;

                Move mov = myDecision.actions.get(pickC).moves.get(pickM);
                Assert.assertNotNull(mov);
                active.makeMove(mov, future);
                Assert.assertNull(future.get(2, SECONDS));
                future = new CompletableFuture<>();

                TimeUnit.MILLISECONDS.sleep(50);
            }

        }
/*
        CompletableFuture<String> future = new CompletableFuture<>();
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.submit(new Runnable() {
            @Override
            public void run() {
              //  future.complete("Hello World!2");
            }
        });
        assertEquals("Hello World!", future.get(1, TimeUnit.SECONDS));
        */
    }

    @Test
    public void theAIGameFullAuto() throws Exception {

        // disable on jenkins
        if (enabled) {

        AIBot a = new AIBot(restTemplate.getRestTemplate(), port);
        assertTrue(a.createUserAccount("Brudi9"));

        AIBot b = new AIBot(restTemplate.getRestTemplate(), port);
        assertTrue(b.createUserAccount("the coder2"));

        a.setMoveTimeout(30);
        b.setMoveTimeout(30);

        a.setAutoPlay(true);
        b.setAutoPlay(true);

        assertTrue(a.createGame());
        assertTrue(b.joinGame(a.getGameId()));

        TimeUnit.MILLISECONDS.sleep(100);

        CompletableFuture future = new CompletableFuture<>();

            CompletableFuture futureAended = new CompletableFuture<>();
            CompletableFuture futureBended = new CompletableFuture<>();

            a.setEnded(futureAended);
            b.setEnded(futureBended);

            // connect the first player to the websocket
            assertTrue(a.joinWebsocket(future));
            Assert.assertNull(future.get(300, SECONDS));

            TimeUnit.MILLISECONDS.sleep(1000);

            future = new CompletableFuture<>();
            b.joinWebsocket(future);
            Assert.assertNull(future.get(300, SECONDS));


            Assert.assertNull(futureAended.get(320, SECONDS));
            Assert.assertNull(futureBended.get(320, SECONDS));

        }
    }

    @Test
    public void AIGameFullWithCardLover() throws Exception {
        // disable on jenkins
        if (true) {

            AIBot a = new AIBot(restTemplate.getRestTemplate(), port);
            assertTrue(a.createUserAccount("Brudi10"));

            AIBot b = new AIBot(restTemplate.getRestTemplate(), port);
            assertTrue(b.createUserAccount("the coder23"));

            a.setMoveTimeout(30);
            b.setMoveTimeout(30);

            a.setAutoPlay(true);
            b.setAutoPlay(true);

            a.strategy = new CardLoverStrategy();
            b.strategy = new CardLoverStrategy();

            assertTrue(a.createGame());
            assertTrue(b.joinGame(a.getGameId()));

            TimeUnit.MILLISECONDS.sleep(100);

            CompletableFuture future = new CompletableFuture<>();

            CompletableFuture futureAended = new CompletableFuture<>();
            CompletableFuture futureBended = new CompletableFuture<>();

            a.setEnded(futureAended);
            b.setEnded(futureBended);

            // connect the first player to the websocket
            assertTrue(a.joinWebsocket(future));
            Assert.assertNull(future.get(300, SECONDS));

            TimeUnit.MILLISECONDS.sleep(1000);

            future = new CompletableFuture<>();
            b.joinWebsocket(future);
            Assert.assertNull(future.get(300, SECONDS));


            Assert.assertNull(futureAended.get(320, SECONDS));
            Assert.assertNull(futureBended.get(320, SECONDS));

        }
    }

    @Test
    public void AIGameFullWithRandomdude() throws Exception {
        // disable on jenkins
        if (true) {

            AIBot a = new AIBot(restTemplate.getRestTemplate(), port);
            assertTrue(a.createUserAccount("Brudi12"));

            AIBot b = new AIBot(restTemplate.getRestTemplate(), port);
            assertTrue(b.createUserAccount("the coder42"));

            a.setMoveTimeout(30);
            b.setMoveTimeout(30);

            a.setAutoPlay(true);
            b.setAutoPlay(true);

            a.strategy = new RandomDudeStrategy();
            b.strategy = new RandomDudeStrategy();

            assertTrue(a.createGame());
            assertTrue(b.joinGame(a.getGameId()));

            TimeUnit.MILLISECONDS.sleep(100);

            CompletableFuture future = new CompletableFuture<>();

            CompletableFuture futureAended = new CompletableFuture<>();
            CompletableFuture futureBended = new CompletableFuture<>();

            a.setEnded(futureAended);
            b.setEnded(futureBended);

            // connect the first player to the websocket
            assertTrue(a.joinWebsocket(future));
            Assert.assertNull(future.get(300, SECONDS));

            TimeUnit.MILLISECONDS.sleep(1000);

            future = new CompletableFuture<>();
            b.joinWebsocket(future);
            Assert.assertNull(future.get(300, SECONDS));


            Assert.assertNull(futureAended.get(320, SECONDS));
            Assert.assertNull(futureBended.get(320, SECONDS));

        }
    }



    @Test
    public void oneBotTooMuch() throws IOException, InterruptedException, TimeoutException, ExecutionException {

        AIBot a = new AIBot(restTemplate.getRestTemplate(), port);
        assertTrue(a.createUserAccount("Brudi8(aka kenny)XXX"));

        AIBot b = new AIBot(restTemplate.getRestTemplate(),port);
        assertTrue(b.createUserAccount("the coderXXX"));

        AIBot c = new AIBot(restTemplate.getRestTemplate(),port);
        assertTrue(c.createUserAccount("the coderXXX222"));
        AIBot d = new AIBot(restTemplate.getRestTemplate(),port);
        assertTrue(d.createUserAccount("the coderXXX333"));

        a.setMoveTimeout(30);
        b.setMoveTimeout(30);
        c.setMoveTimeout(30);
        d.setMoveTimeout(30);


        a.setAutoPlay(false);
        b.setAutoPlay(false);
        c.setAutoPlay(false);
        d.setAutoPlay(false);

        assertTrue(a.createGame());
        assertTrue(b.joinGame(a.getGameId()));

        TimeUnit.MILLISECONDS.sleep(150);


        CompletableFuture future = new CompletableFuture<>();

        // disable on jenkins

        // connect the first player to the websocket
        assertTrue(a.joinWebsocket(future));



        future = new CompletableFuture<>();
        assertTrue(b.joinWebsocket(future));
        Assert.assertNull(future.get(60, SECONDS));


        // connect the third player to the websocket
        c.game = b.game;
        future = new CompletableFuture<>();
        Assert.assertTrue(c.joinWebsocket(future));
        Assert.assertNull(future.get(60, SECONDS));

        d.game = b.game;
        future = new CompletableFuture<>();
        Assert.assertTrue(d.joinWebsocket(future));
        Assert.assertNull(future.get(60, SECONDS));



    }

    @Autowired
    void setConverters(HttpMessageConverter<?>[] converters) {

        this.mappingJackson2HttpMessageConverter = asList(converters).stream()
                .filter(hmc -> hmc instanceof MappingJackson2HttpMessageConverter)
                .findAny()
                .orElse(null);

        assertNotNull("the JSON message converter must not be null",
                this.mappingJackson2HttpMessageConverter);
    }

    protected String json(Object o) throws IOException {
        MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
        this.mappingJackson2HttpMessageConverter.write(
                o, MediaType.APPLICATION_JSON, mockHttpOutputMessage);
        return mockHttpOutputMessage.getBodyAsString();
    }
}
