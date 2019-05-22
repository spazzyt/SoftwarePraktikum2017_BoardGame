package ch.uzh.ifi.seal.soprafs17;

import ch.uzh.ifi.seal.soprafs17.web.websockets.WebsocketCtrl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.*;


@Configuration
@EnableWebSocket
public class WebSocketConfiguration implements WebSocketConfigurer {

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(greetingHandler(), "/websocket").setAllowedOrigins("*");
    }

    @Bean
    public WebSocketHandler greetingHandler() {
        return new WebsocketCtrl();
    }
}

/*
@Configuration
@EnableWebSocketMessageBroker
class WebSocketConfiguration extends AbstractWebSocketMessageBrokerConfigurer {
    public void configureMessageBroker(MessageBrokerRegistry config) {
        ThreadPoolTaskScheduler te = new ThreadPoolTaskScheduler();
        te.setPoolSize(1);
        te.setThreadNamePrefix("wss-heartbeat-thread-");
        te.initialize();

        //config.enableSimpleBroker("/topic").setTaskScheduler(new ConcurrentTaskScheduler());
        config.enableStompBrokerRelay("/topic/");
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry stompEndpointRegistry) {
        stompEndpointRegistry.addEndpoint("/websocket").setAllowedOrigins("*");
    }

    @Bean(initMethod = "start", destroyMethod = "stop")
    public BrokerService broker() throws Exception {
        final BrokerService broker = new BrokerService();
        //broker.addConnector("tcp://localhost:61616");
        broker.addConnector("stomp://localhost:61613");
        broker.addConnector("vm://localhost");
        return broker;
    }
}

*/