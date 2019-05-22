package ch.uzh.ifi.seal.soprafs17.web.rest;

import ch.uzh.ifi.seal.soprafs17.entity.Game;
import ch.uzh.ifi.seal.soprafs17.entity.Move;
import ch.uzh.ifi.seal.soprafs17.entity.User;
import ch.uzh.ifi.seal.soprafs17.gamelogic.Decision;
import ch.uzh.ifi.seal.soprafs17.gamelogic.Player;
import ch.uzh.ifi.seal.soprafs17.repository.GameRepository;
import ch.uzh.ifi.seal.soprafs17.repository.UserRepository;
import ch.uzh.ifi.seal.soprafs17.service.GameService;
import ch.uzh.ifi.seal.soprafs17.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@RestController
public class GameResource extends GenericResource {

    Logger                 logger  = LoggerFactory.getLogger(GameResource.class);

    @Autowired
    private UserRepository userRepo;
    @Autowired
    private GameRepository gameRepo;
    @Autowired
    private GameService gameService;

    private final String   CONTEXT = "/game";


    @Autowired
    private UserService userService;

    /*??????
     * Context: /game
     */

    @RequestMapping(value = CONTEXT)
    @ResponseStatus(HttpStatus.OK)
    public List<Game> listGames() {
        logger.debug("listGames");
        List<Game> result = new ArrayList<>();
        gameRepo.findAll().forEach(result::add);
        return result;
    }




    @RequestMapping(value = CONTEXT + "/{gameId}/join", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> joinGame(@PathVariable Long gameId, @RequestParam("token") String userToken) {
        logger.debug("addPlayer: " + userToken);
        Game game = gameRepo.findOne(gameId);
        if(game == null) {
            logger.warn("Game not found:"+ gameId);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        User player = userRepo.findByToken(userToken);


        if (gameService.joinGame(game,player)) {
            return new ResponseEntity<>(game, HttpStatus.OK);
        } else {
            logger.error("Error adding player with token: " + userToken);
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }




    @RequestMapping(value = CONTEXT + "/{gameId}/leave", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> leaveGame(@PathVariable Long gameId, @RequestParam("token")String userToken){

        logger.debug("leaveGame:" + gameId);

        Game game = gameRepo.findOne(gameId);
        User player = userRepo.findByToken(userToken);

        if (player != null && game != null) {
            gameService.leaveGame(game,player);
            return new ResponseEntity<>(CONTEXT, HttpStatus.OK);
        }
        return new ResponseEntity<>( HttpStatus.NOT_FOUND);
    }




    //fehlt noch ein parameter: siehe report 2 (abgegeben) wäre noch int maxplayers
    @RequestMapping(value = CONTEXT, method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> addGame(@RequestBody Game game, @RequestParam("token") String userToken) {
        logger.debug("addGame: " + game);

        //User owner = userRepo.findByToken(userToken);
        Game theGame = gameService.createGame(userToken,game.getNumPlayers());

        if(theGame != null)
            return new ResponseEntity<>(theGame, HttpStatus.CREATED);
        else
            return new ResponseEntity<>( HttpStatus.FORBIDDEN);

    }




    @RequestMapping(value = "/pepe", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public String pepe(@RequestParam("token") String userToken) {

        logger.error("pepe game:  " + userToken);

        /*
        Game elPepeGameQuickScope = gameService.createGame(userToken,2);




        if (elPepeGameQuickScope != null && gameService.pepe(elPepeGameQuickScope)) {
            res = elPepeGameQuickScope.getId().toString();
        }
        */

        return "1";
    }


    @RequestMapping(value = "/bob", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public String bob(@RequestParam("token") String userToken) {

        return "1"; //gameService.bob();

    }


    @RequestMapping(value = CONTEXT + "/{gameId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> getGame(@PathVariable Long gameId) {
        logger.debug("getGame: " + gameId);

        Game game = gameRepo.findOne(gameId);
        if(game == null) {
            logger.warn("game not found:"+gameId);
            return new ResponseEntity<>( HttpStatus.NOT_FOUND);
        }
        User newUser = new User();
        newUser.setUsername("yoo");



        return new ResponseEntity<>(game, HttpStatus.OK);
    }




    @RequestMapping(value = CONTEXT + "/{gameId}/actions", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> getDecisionForPlayer(@RequestParam("token") String userToken, @PathVariable Long gameId){

        Game theGame = gameService.getRunningGame(gameId);
        if (theGame != null) {

            Player p = theGame.getPlayer(userToken);
            if(p != null) {
                logger.debug("Found a decision: " + p.decision);

                return new ResponseEntity<>(p.decision, HttpStatus.OK);

            } else {
                logger.debug("User not found in game: " + userToken);
            }
        } else {
            logger.debug("Game not found: " + gameId);
        }

        return new ResponseEntity<>( HttpStatus.NOT_FOUND);
    }




    @RequestMapping(value = CONTEXT + "/{gameId}/move", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> makeMove(@PathVariable Long gameId, @RequestBody Move move, @RequestParam("token") String userToken) {
        logger.debug("addMove: " + move);

        Game game = gameService.getRunningGame(gameId);
        User user = userRepo.findByToken(userToken);

        // does the player exists in the first place?
        if(user == null)
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);

        if(game == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        // get the player out of the game object
        User player = game.getUser(user.getToken());


        game = gameService.getRunningGame(gameId);
        move.setUser(player);

            if(!game.turnManager.executePlayerMove(move)) {
                logger.warn("could not execute Move: " + move);
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }


        Decision res = player.player.decision;
        if(res== null)
            res = new Decision();

        return new ResponseEntity<>(res,HttpStatus.CREATED);

    }




}