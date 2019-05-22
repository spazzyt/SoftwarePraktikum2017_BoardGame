package ch.uzh.ifi.seal.soprafs17.bot;

import ch.uzh.ifi.seal.soprafs17.gamelogic.actions.Action;
import ch.uzh.ifi.seal.soprafs17.gamelogic.Decision;
import ch.uzh.ifi.seal.soprafs17.entity.Move;
import ch.uzh.ifi.seal.soprafs17.web.websockets.messages.ConnectToGame;
import ch.uzh.ifi.seal.soprafs17.web.websockets.messages.WSMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;




public class SimpleClientWebSocketHandler extends TextWebSocketHandler {

    private final AIBot bot;
    protected Log logger = LogFactory.getLog(SimpleClientWebSocketHandler.class);

    private CompletableFuture<String> future;



    private Random random = new Random();

    private List<WSMessage> messages = new ArrayList<WSMessage>();

    public SimpleClientWebSocketHandler(CompletableFuture<String> future, AIBot bot) {
        this.future = future;
        this.bot = bot;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {

        this.logger.info("ClientConnected");

        this.logger.info("sleep");
        Thread.sleep(200);
        this.logger.info("done sleeping");


        ConnectToGame cg = new ConnectToGame();

        cg.gameId = bot.getGameId();
        cg.user = bot.user.getUsername();

        this.logger.info("ConnectMsg: " + cg.toString());

        ObjectMapper mapper = new ObjectMapper();

        String serialized = mapper.writeValueAsString(cg);
        this.logger.info("ConnectMsg2: " + serialized);

        TextMessage message = new TextMessage(serialized);
        this.logger.info("prepared msg");

        session.sendMessage(message);
        this.logger.info("msg sent");

        future.complete("CONNECTED");
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message)
            throws Exception {
        this.logger.info("Received: " + message.getPayload() + " ( )");

        if(message.getPayload().equals("GAME NOT FOUND")) {
            this.logger.info("Game is not found");
            return;
        }

        ObjectMapper mapper = new ObjectMapper();
        WSMessage msg = mapper.readValue(message.getPayload(), WSMessage.class);

        assertNotNull(msg);

        messages.add(msg);

        Thread.sleep(50);

        switch (msg.type) {
            case OK:
                break;

            case CURRENTTURN:
                HashMap<String,String> mm = (HashMap<String, String>) msg.payload;
                String who = mm.get("user");
                if( who.equals(bot.user.getUsername())) {
                    handleTurn();

                } else {
                    this.logger.info("It's NOT my turn!!: ");
                }
                break;

            case ENDGAME:
                this.logger.info(" Game ended");
                if(bot.ended != null) {
                    bot.ended.complete(null);
                }
                break;

            case PICKCARD:
                this.logger.info(" Smb should pick a card");
                HashMap<String,String> mpc = (HashMap<String, String>) msg.payload;
                String whopc = mpc.get("user");
                if( whopc.equals(bot.user.getUsername())) {

                    this.logger.info(" I should pick");
                    handleTurn();
                }

                break;

            case NEWROUND:
                this.logger.info("It's a new round ");
                break;
        }

        //assertEquals("Ok", message.getPayload());



        //session.close();
        // this.messagePayload.set(message.getPayload());
        // this.latch.countDown();
    }

    private void handleTurn() throws IOException, InterruptedException, TimeoutException, ExecutionException {
        this.logger.info("It's my turn!!: ");
        CompletableFuture<Object> desFuture = new CompletableFuture<>();
        Decision des = bot.getAvailableDecision(desFuture);
        desFuture.complete(null);

        if(des == null) {
            this.logger.warn("My Decision is null????, maybe not my turn?");
            return;
        }
        this.logger.info("Got a decsison: "+ des.actions.size());

        Move m = bot.strategy.pickMove(des);

       /* if(bot.isAutoPlay()) {
            m = des.actions.get(0).moves.get(this.random.nextInt(des.actions.get(0).moves.size()));
        } else {
            m = des.actions.get(0).moves.get(0);
        } */


        if(bot.isAutoPlay()) {
            desFuture = new CompletableFuture<>();
            bot.makeMove(m, desFuture);
            this.logger.info("Made a move " + m.toString());
            desFuture.get(10, TimeUnit.SECONDS);


            tryMultiMove();
        }


    }

    private void tryMultiMove() throws IOException, InterruptedException, ExecutionException, TimeoutException {
        CompletableFuture<Object> desFuture;
        Move m;
        if(bot.myDecision != null && bot.myDecision.actions.size() > 0) {
            this.logger.info("I can make more moves! " + bot.myDecision.actions.size());
            for(Action a : bot.myDecision.actions)
                this.logger.debug("Ac: " + a.type + " :" + a.moves.size());

            desFuture = new CompletableFuture<>();
            m = bot.strategy.pickMove(bot.myDecision);
            this.logger.info("makingMove:  " + m);

            bot.makeMove(m, desFuture);
            this.logger.info("makeMove again");
            desFuture.get(5, TimeUnit.SECONDS);

            tryMultiMove();
        }
    }

    public List<WSMessage> getMessages() {
        return messages;
    }

}
