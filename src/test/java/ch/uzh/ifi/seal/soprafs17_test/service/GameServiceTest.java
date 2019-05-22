package ch.uzh.ifi.seal.soprafs17.service;

import ch.uzh.ifi.seal.soprafs17.Application;
import ch.uzh.ifi.seal.soprafs17.entity.Game;
import ch.uzh.ifi.seal.soprafs17.entity.User;
import ch.uzh.ifi.seal.soprafs17.repository.GameRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;



/*

setup() throws Exception
void createGame()

*/



@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@Transactional
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class GameServiceTest {

    @Qualifier("gameRepository")
    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private GameService gameService;

    @Autowired
    private UserService userService;

    private User user;

    private Game game;

    @Before
    public void setup() throws Exception {


        game = gameRepository.findByName("brudiF");

    }

    @Test
    public void createGame(){
        user = userService.createUser("brudiF");

        Assert.assertNull(game);

        // wrong user
        game = gameService.createGame("abcdefg",2);
        Assert.assertNull(game);

        game = gameService.createGame(user.getToken(),2);
        Assert.assertNotNull(game);

        Long gameId = game.getId();
        Assert.assertNotNull(gameId);

        Assert.assertNotNull(game);
        Assert.assertEquals(game.getPlayers().size(),1);


        Game g2 = gameRepository.findOne(gameId);

        Assert.assertEquals(game.getName(),g2.getName());
        Assert.assertNotNull(g2);
        Assert.assertEquals(g2.players.size(),1);
        Assert.assertEquals(g2.getPlayers().size(),1);

        Assert.assertEquals(1, user.getGames().size());


    }

    @Test
    public void createFullGame(){
        User user1 = userService.createUser("brudiF1");
        User user2 = userService.createUser("brudiF2");

        game = gameService.createGame(user1.getToken(),2);
        Assert.assertTrue(gameService.joinGame(game,user2));

        Game game3 = gameRepository.findOne(game.getId());
        Assert.assertNotNull(game3);
        Assert.assertEquals(2, game3.getPlayers().size());

        Assert.assertEquals("brudiF2", game3.getPlayers().get(1).getUsername());

        gameService.startGame(game);

        Game game2 = gameService.getRunningGame(game.getId());
        // no ws here so start manually
        game2.setUpGame();
        game2.start();

        Assert.assertNotNull(game2);
        Assert.assertEquals(2,game2.getPlayers().size());

    }

    @Test
    public void joinGameFail() {

        User user1 = userService.createUser("brudiF11");
        User user2 = userService.createUser("brudiF21");

        game = gameService.createGame(user1.getToken(),2);
        Assert.assertTrue(gameService.joinGame(game,user2));

        // user is already joined
        Assert.assertFalse(gameService.joinGame(game,user1));
    }


    @Test
    public void leaveGameFail() {
        User user1 = userService.createUser("brudiF11");
        User user2 = userService.createUser("brudiF21");

        game = gameService.createGame(user1.getToken(),2);
        Assert.assertFalse(gameService.leaveGame(game,user2));
    }

}
