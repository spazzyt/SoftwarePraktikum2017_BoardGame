package ch.uzh.ifi.seal.soprafs17.service;

import ch.uzh.ifi.seal.soprafs17.bot.AIBot;
import ch.uzh.ifi.seal.soprafs17.bot.strategies.CardLoverStrategy;
import ch.uzh.ifi.seal.soprafs17.constant.GameStatus;
import ch.uzh.ifi.seal.soprafs17.entity.Game;
import ch.uzh.ifi.seal.soprafs17.entity.User;
import ch.uzh.ifi.seal.soprafs17.repository.GameRepository;
import ch.uzh.ifi.seal.soprafs17.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.*;

/**
 Service class for managing users.

 GameService(GameRepository gameRepository, UserRepository userRepository)
 Game createGameForPlayer(String token, Integer numPlayers)
 Boolean joinGame(Game game, User player)
 Boolean leaveGame(Game game, User player)

 */
@Service
@Transactional
public class GameService {

    private final Logger log = LoggerFactory.getLogger(UserService.class);
    private final GameRepository gameRepository;
    private final UserRepository userRepository;


    private final Map<Long, Game> runningGames = new HashMap<>();

    private final List<AIBot> aibots = new ArrayList<>();

   // @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private RestTemplateBuilder restTemplateBuilder;
    
    @Autowired
    Environment environment;

    @Autowired
    public GameService(GameRepository gameRepository, UserRepository userRepository) {
        this.gameRepository = gameRepository;
        this.userRepository = userRepository;
    }


    //creates a game and sets an owner if the owner is not null
    public Game createGame(String token, Integer numPlayers) {
        //UserRepository userRepo = null; (is this needed)
        User owner = userRepository.findByToken(token);

        if (owner != null) {
            Game newGame = new Game();
            newGame.addPlayer(owner);
            newGame.setNumPlayers(numPlayers);
            newGame.setOwner(owner.getUsername());
            newGame.setName(owner.getUsername());
            newGame.setStatus(GameStatus.PENDING);
            newGame = gameRepository.save(newGame);
            // user.games now contains the new game
            userRepository.save(owner);
            return newGame;
        }
        return null;
    }

    //joins a game if there's a player and a game with empty slots
    //why boolean and not a game that gets returned
    public Boolean joinGame(Game game, User player) {
        if (game != null && player != null && game.getPlayers().size() < game.getNumPlayers() && !game.getPlayers().contains(player)) {
            game.addPlayer(player);


            log.debug("Game: " + game.getName() + " - player added: " + player.getUsername());
            game = gameRepository.save(game);
            if (game.getPlayers().size() == game.getNumPlayers()) {
                game.setStatus(GameStatus.STARTING);
                game = gameRepository.save(game);
                startGame(game);
            }
            return true;
        } else {
            if(player != null)
                log.error("Error adding player with token: " + player.getToken());
            else
                log.error("Error adding player with 'TOKEN NOT FOUND'.");
            return false;
        }


    }


    //leaves a game if the game exists and actually contains this player
    public Boolean leaveGame(Game game, User player) {

        if (game != null && player != null && game.getPlayers().contains(player)) {
            game.removePlayer(player);
            gameRepository.save(game);
            if (game.getPlayers().size() == 0) {
                player.deleteGameById(game.getId());
                userRepository.save(player);
                gameRepository.delete(game);
            }


            return true;
        } else {
            log.error("Error leaving game " + player);
            return false;
        }
    }

   /* public Game getGame(Long id) {
        return gameRepository.findOne(id);
    } */

    /*
     * Non-DB: running games
     */



    /*
     * once the game is full, start it
     */
    public Boolean startGame(Game game) {

        runningGames.put(game.getId(), game);
        game.setNumPlayers(game.getPlayers().size());


        return true;
    }




    public Game getRunningGame(Long gameId) {

        return runningGames.get(gameId);
    }


    public void playerConnect(Long gameId, String user, WebSocketSession session) throws IOException {
        Game g = getRunningGame(gameId);
        g.addWSSession(user, session);

        log.debug("playerConnect: + " + user);

        // all players are connected now!
        if(g.wsSessions.size() == g.getNumPlayers()) {
            g.setStatus(GameStatus.RUNNING);
            gameRepository.save(g);

            g.setUpGame();
            g.start();
            log.debug("gameStarted");
        } else if (g.wsSessions.size() > g.getNumPlayers()) {
            log.debug("It's a Bob game");

            for(TextMessage tm : g.msgLog) {
                session.sendMessage(tm);
                log.debug("Replay WS: "+ tm);
            }

        }


    }

/*
    public boolean pepe(Game elPepeGameQuickScope) throws Exception {

        String port = environment.getProperty("local.server.port");

        restTemplateBuilder = new RestTemplateBuilder()
                .rootUri("http://localhost:"+ port);

        restTemplate = restTemplateBuilder.build();
        log.warn("restTemplate on?: " + port);


        //blazeit420 is bot
        AIBot blazeit420 = new AIBot(restTemplate,Integer.valueOf(port));

        Random rand = new Random();
        boolean status;

            do {
                status = !blazeit420.createUserAccount("Bob" + Integer.toString(rand.nextInt(42)));

            } while (status);


            if (!blazeit420.joinGame(elPepeGameQuickScope.getId())) {
                log.error("can't join pepe: " + elPepeGameQuickScope.getId());
                return false;
            }

            CompletableFuture future = new CompletableFuture<>();
            if (!blazeit420.joinWebsocket(future))
                return false;


        aibots.add(blazeit420);

        return true;
    }
*/

    /*
    public String bob() throws Exception {

        String port = environment.getProperty("local.server.port");

        restTemplateBuilder = new RestTemplateBuilder()
                .rootUri("http://localhost:"+ port);

        restTemplate = restTemplateBuilder.build();
        log.warn("restTemplate on?: " + port);

        //blazeit420 is bot
        AIBot bobBot = new AIBot(restTemplate,Integer.valueOf(port));

        boolean status;
        Random rand = new Random();



            do {
                status = !bobBot.createUserAccount("Bob" + Integer.toString(rand.nextInt(42)));

            } while (status);

            if (!bobBot.createGame()) {
                return null;
            }


            bobBot.strategy = new CardLoverStrategy();

            //blazeit420 is bot
            AIBot blazeit420 = new AIBot(restTemplate, Integer.valueOf(port));



            do {
                status = !blazeit420.createUserAccount("Herman" + Integer.toString(rand.nextInt(42)));

            } while (status);

            if (!blazeit420.joinGame(bobBot.getGameId())) {
                log.error("can't join pepe: " + bobBot.getGameId());
                return null;
            }

            blazeit420.strategy = new CardLoverStrategy();

            CompletableFuture future = new CompletableFuture<>();

            if (!bobBot.joinWebsocket(future))
                return null;

            future.get(1, SECONDS);

            future = new CompletableFuture<>();

            if (!blazeit420.joinWebsocket(future))
                return null;

            future.get(1, SECONDS);


        aibots.add(bobBot);
        aibots.add(blazeit420);

       // return bobBot.getGameId().toString();
        return "1";
    }  */

}
