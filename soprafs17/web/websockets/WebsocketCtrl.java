package ch.uzh.ifi.seal.soprafs17.web.websockets;


import ch.uzh.ifi.seal.soprafs17.entity.Game;
import ch.uzh.ifi.seal.soprafs17.service.GameService;
import ch.uzh.ifi.seal.soprafs17.web.websockets.messages.ConnectToGame;
import ch.uzh.ifi.seal.soprafs17.web.websockets.messages.WSMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;

public class WebsocketCtrl extends TextWebSocketHandler {

    private static final Log logger = LogFactory.getLog(WebsocketCtrl.class);

    @Autowired
    private GameService gameService;

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws InterruptedException, IOException {

        logger.debug("incoming msg: "+ message.getPayload());

        ObjectMapper mapper = new ObjectMapper();
        ConnectToGame req = mapper.readValue(message.getPayload(), ConnectToGame.class);

        TextMessage msg;

        Game g = gameService.getRunningGame(req.gameId);
        if(g == null) {
            logger.warn("Game not found: " + req.gameId);
            msg = new TextMessage("GAME NOT FOUND");
            session.sendMessage(msg);

        } else {

            // user connected to game X

            WSMessage m = new WSMessage();
            m.type = WSMessage.MessageType.OK;

            msg = new TextMessage(mapper.writeValueAsString(m));
            session.sendMessage(msg);

            gameService.playerConnect(req.gameId, req.user, session);

        }

    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        logger.debug("connected: "+ session.toString());
    }

}