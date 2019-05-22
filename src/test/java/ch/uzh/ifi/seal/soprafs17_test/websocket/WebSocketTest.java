package ch.uzh.ifi.seal.soprafs17.websocket;

import ch.uzh.ifi.seal.soprafs17.Application;
import ch.uzh.ifi.seal.soprafs17.entity.Game;
import ch.uzh.ifi.seal.soprafs17.entity.User;
import ch.uzh.ifi.seal.soprafs17.bot.AIBot;
import ch.uzh.ifi.seal.soprafs17.bot.SimpleClientWebSocketHandler;
import ch.uzh.ifi.seal.soprafs17.web.websockets.messages.ConnectToGame;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.StatusCode;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.eclipse.jetty.websocket.client.ClientUpgradeRequest;
import org.eclipse.jetty.websocket.client.WebSocketClient;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.socket.client.WebSocketConnectionManager;
import org.springframework.web.socket.client.jetty.JettyWebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.sockjs.client.RestTemplateXhrTransport;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

import static java.util.Arrays.asList;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;


/*
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {
        WebSocketTest.TestWebSocketConfig.class,
        WebSocketTest.TestConfig.class
})
@WebAppConfiguration
public class WebSocketTest {

    @Autowired private AbstractSubscribableChannel clientInboundChannel;

    @Autowired private AbstractSubscribableChannel clientOutboundChannel;

    @Autowired private AbstractSubscribableChannel brokerChannel;

    private TestChannelInterceptor clientOutboundChannelInterceptor;

    private TestChannelInterceptor brokerChannelInterceptor;


    @Before
    public void setUp() throws Exception {

        this.brokerChannelInterceptor = new TestChannelInterceptor();
        this.clientOutboundChannelInterceptor = new TestChannelInterceptor();

        this.brokerChannel.addInterceptor(this.brokerChannelInterceptor);
        this.clientOutboundChannel.addInterceptor(this.clientOutboundChannelInterceptor);
    }



    @Test
    public void gameJoinStart() {

        MockGame g = new MockGame();

        MockPlayer p1 = new MockPlayer();
        MockPlayer p1 = new MockPlayer();

        g.join(p1);
        g.join(p1);
        assertEquals(WAITING_FOR_PLAYERS,g.status);

        p1.subWS();
        p2.subWS();
        assertEquals(RUNNING,g.status);

        p1.isCurrentPlayer();
        Actions a = p1.getValidActions();
        Move m = a.pickRandom();
        p1.play(m);

        p1.recvMove() == m

    }

    @Test
    public void getPositions() throws Exception {

        StompHeaderAccessor headers = StompHeaderAccessor.create(StompCommand.SUBSCRIBE);
        headers.setSubscriptionId("0");
        headers.setDestination("/topic/game/2");
        headers.setSessionId("0");
        //headers.setUser(new TestPrincipal("fabrice"));
        headers.setSessionAttributes(new HashMap<String, Object>());
        Message<byte[]> message = MessageBuilder.createMessage(new byte[0], headers.getMessageHeaders());

        this.clientOutboundChannelInterceptor.setIncludedDestinations("/topic/game/**");
        this.clientInboundChannel.send(message);

        this.brokerChannelInterceptor.setIncludedDestinations("/topic/game/**");

      //  Message<?> reply1 = this.brokerChannelInterceptor.awaitMessage(5);
     //   assertNotNull(reply1);


        byte[] payload = new ObjectMapper().writeValueAsBytes("aha");

        StompHeaderAccessor headersJoin = StompHeaderAccessor.create(StompCommand.SEND);
        headersJoin.setDestination("/app/game/2");
        headersJoin.setSessionId("0");
        headersJoin.setSessionAttributes(new HashMap<String, Object>());
        Message<byte[]> messagejoin = MessageBuilder.createMessage(payload, headers.getMessageHeaders());

       // this.brokerChannelInterceptor.setIncludedDestinations("/user/**");
        this.clientInboundChannel.send(messagejoin);



        this.brokerChannelInterceptor.setIncludedDestinations("/topic/**");
      //  Message<?> reply = this.brokerChannelInterceptor.awaitMessage(5);
        Message<?> reply = this.clientOutboundChannelInterceptor.awaitMessage(5);
        assertNotNull(reply);

        StompHeaderAccessor replyHeaders = StompHeaderAccessor.wrap(reply);
        assertEquals("0", replyHeaders.getSessionId());
        assertEquals("0", replyHeaders.getSubscriptionId());
        assertEquals("/app/game/42", replyHeaders.getDestination());

        String json = new String((byte[]) reply.getPayload(), Charset.forName("UTF-8"));

        assertEquals("yo", json);
       /* new JsonPathExpectationsHelper("$[0].company").assertValue(json, "Citrix Systems, Inc.");
        new JsonPathExpectationsHelper("$[1].company").assertValue(json, "Dell Inc.");
        new JsonPathExpectationsHelper("$[2].company").assertValue(json, "Microsoft");
        new JsonPathExpectationsHelper("$[3].company").assertValue(json, "Oracle");
        *
}
import static org.junit.Assert.assertThat;
/*  @Test
    public void executeTrade() throws Exception {

        Trade trade = new Trade();
        trade.setAction(Trade.TradeAction.Buy);
        trade.setTicker("DELL");
        trade.setShares(25);

        byte[] payload = new ObjectMapper().writeValueAsBytes(trade);

        StompHeaderAccessor headers = StompHeaderAccessor.create(StompCommand.SEND);
        headers.setDestination("/app/trade");
        headers.setSessionId("0");
        headers.setUser(new TestPrincipal("fabrice"));
        headers.setSessionAttributes(new HashMap<String, Object>());
        Message<byte[]> message = MessageBuilder.createMessage(payload, headers.getMessageHeaders());

        this.brokerChannelInterceptor.setIncludedDestinations("/user/**");
        this.clientInboundChannel.send(message);

        Message<?> positionUpdate = this.brokerChannelInterceptor.awaitMessage(5);
        assertNotNull(positionUpdate);

        StompHeaderAccessor positionUpdateHeaders = StompHeaderAccessor.wrap(positionUpdate);
        assertEquals("/user/fabrice/queue/position-updates", positionUpdateHeaders.getDestination());

        String json = new String((byte[]) positionUpdate.getPayload(), Charset.forName("UTF-8"));
        new JsonPathExpectationsHelper("$.ticker").assertValue(json, "DELL");
        new JsonPathExpectationsHelper("$.shares").assertValue(json, 75);
    }


    @Configuration
    @EnableScheduling
    @ComponentScan(
            basePackages="ch.uzh.ifi.seal.soprafs17.web.websockets",
            excludeFilters = @ComponentScan.Filter(type= FilterType.ANNOTATION, value = Configuration.class)
    )
    @EnableWebSocketMessageBroker
    static class TestWebSocketConfig extends AbstractWebSocketMessageBrokerConfigurer {

        @Autowired
        Environment env;

        @Override
        public void registerStompEndpoints(StompEndpointRegistry registry) {
            registry.addEndpoint("/websocket").withSockJS();
        }

        @Override
        public void configureMessageBroker(MessageBrokerRegistry registry) {
			registry.enableSimpleBroker("/queue/", "/topic/");
            //registry.enableStompBrokerRelay("/queue/", "/topic/");
            registry.setApplicationDestinationPrefixes("/app");
        }
    }

    /**
     * Configuration class that un-registers MessageHandler's it finds in the
     * ApplicationContext from the message channels they are subscribed to...
     * except the message handler used to invoke annotated message handling methods.
     * The intent is to reduce additional processing and additional messages not
     * related to the test.
     */
/*
    @Configuration
    @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
    static class TestConfig implements ApplicationListener<ContextRefreshedEvent> {

        @Autowired
        private List<SubscribableChannel> channels;

        @Autowired
        private List<MessageHandler> handlers;


        @Override
        public void onApplicationEvent(ContextRefreshedEvent event) {
            for (MessageHandler handler : handlers) {
                if (handler instanceof SimpAnnotationMethodMessageHandler) {
                    continue;
                }
                for (SubscribableChannel channel :channels) {
                    channel.unsubscribe(handler);
                }
            }
        }
    }
}


*/

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class WebSocketTest {

    @Value("${local.server.port}")
    private int port;

    private WebSocketClient client; // from jetty
    private URI uri;
    private SimpleEchoSocket socket;

    @Autowired
    private TestRestTemplate restTemplate;

    private HttpMessageConverter mappingJackson2HttpMessageConverter;

    @Before
    public void setup() throws Exception {
        List<Transport> transports = new ArrayList<>(2);
        transports.add(new WebSocketTransport(new StandardWebSocketClient()));
        transports.add(new RestTemplateXhrTransport());

        client = new WebSocketClient();
         socket = new SimpleEchoSocket();

        uri = new URI("ws://localhost:" + port + "/websocket");
        client.start();
    }

    public void cleanup() throws Exception {
        client.stop();
    }

    @Test
    public void testConnect() throws Exception {

        Future<Session> sessionFuture = client.connect(socket, uri, new ClientUpgradeRequest());
        Session session = sessionFuture.get(5, SECONDS);


        //session.getRemote().sendString();

        // message exchange need some time
        TimeUnit.MILLISECONDS.sleep(500);
       assertThat(socket.getMessages().get(0)).isEqualTo("GAME NOT FOUND");

      //  assertThat(socket.isConnected()).isTrue();
    }


    @Test
    public void testBotConnect() throws InterruptedException, ExecutionException, TimeoutException {

        CompletableFuture<String> futureC = new CompletableFuture<>();
        AIBot bot = new AIBot(restTemplate.getRestTemplate(), port);
        bot.game = new Game();
        bot.user = new User();

        SimpleClientWebSocketHandler handler = new SimpleClientWebSocketHandler(futureC, bot);
        JettyWebSocketClient sclient = new JettyWebSocketClient();

        WebSocketConnectionManager manager = new WebSocketConnectionManager(sclient,
                handler, "ws://localhost:" + port + "/websocket");


        manager.start();
        //   assertEquals("CONNECTED", futureC.get(2, SECONDS));




        Assert.assertTrue(futureC.get(60, SECONDS).equals("CONNECTED"));

        TimeUnit.MILLISECONDS.sleep(500);
    }


    /**
     * Basic Echo Client Socket
     */
    @WebSocket(maxTextMessageSize = 64 * 1024)
    public class SimpleEchoSocket
    {
        private final CountDownLatch closeLatch;
        @SuppressWarnings("unused")
        private Session session;

        private List<String> messages = new ArrayList<>();

        public SimpleEchoSocket()
        {
            this.closeLatch = new CountDownLatch(1);
        }

        public boolean awaitClose(int duration, TimeUnit unit) throws InterruptedException
        {
            return this.closeLatch.await(duration,unit);
        }

        @OnWebSocketClose
        public void onClose(int statusCode, String reason)
        {
            System.out.printf("Connection closed: %d - %s%n",statusCode,reason);
            this.session = null;
            this.closeLatch.countDown(); // trigger latch
        }

        @OnWebSocketConnect
        public void onConnect(Session session)
        {
            System.out.printf("Got connect: %s%n",session);
            this.session = session;
            try
            {

                ConnectToGame cg = new ConnectToGame();
                cg.gameId = 123l;


                String msg = json(cg);

                Future<Void> fut;
                fut = session.getRemote().sendStringByFuture(msg);
                fut.get(2, SECONDS); // wait for send to complete.

/*
                fut = session.getRemote().sendStringByFuture("Thanks for the conversation.");
                fut.get(2,TimeUnit.SECONDS); // wait for send to complete.

                */


            }
            catch (Throwable t)
            {
                t.printStackTrace();
            }
        }

        @OnWebSocketMessage
        public void onMessage(String msg)
        {
            System.out.printf("Got msg: %s%n",msg);
            messages.add(msg);

            session.close(StatusCode.NORMAL,"I'm done");
        }

        public boolean isConnected() {
            return session != null && session.isOpen();
        }

        public List<String> getMessages() {
            return messages;
        }
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
