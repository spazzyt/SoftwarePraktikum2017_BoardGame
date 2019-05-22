package ch.uzh.ifi.seal.soprafs17.gamelogic.sites;

import ch.uzh.ifi.seal.soprafs17.Application;
import ch.uzh.ifi.seal.soprafs17.constant.PlayerFieldColour;
import ch.uzh.ifi.seal.soprafs17.entity.Game;
import ch.uzh.ifi.seal.soprafs17.entity.Move;
import ch.uzh.ifi.seal.soprafs17.entity.User;
import ch.uzh.ifi.seal.soprafs17.gamelogic.Pyramid;
import ch.uzh.ifi.seal.soprafs17.gamelogic.TurnManager;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;

/**
 * Created by Spasen on 07.04.17.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
public class PyramidTest {


    private Game g;
    private User u1;
    private User u2;

    @Test
    public void testInitArray(){
        Pyramid pyramid1 = new Pyramid();


        //2, 1, 3, 2, 4, 3, 2, 1, 3,  2,  3,  1,  3,  4
        //1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14

        assertEquals(pyramid1.fields[0].points,2);
        assertEquals(pyramid1.fields[1].points,1);
        assertEquals(pyramid1.fields[3].points,2);
        assertEquals(pyramid1.fields[13].points,4);
        assertEquals(pyramid1.fields[15].points,1);


    }


    //2, 1, 3, 2, 4, 3, 2, 1, 3,  2,  3,  1,  3,  4
    //1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14
    @Test
    public void testSetStone() throws Exception{

        Move m = new Move();
        u1 = new User("pepe");
        u2 = new User("bob");

        g = new Game();
        g.setNumPlayers(2);
        g.addPlayer(u1);
        g.addPlayer(u2);

        g.setUpGame();
        g.start();

        TurnManager tm = g.turnManager;
        Pyramid pyramid = (Pyramid) g.siteBoards.get("Pyramid");

        pyramid.setStone(u1.player.colour);//2
        pyramid.setStone(u1.player.colour);//1
        pyramid.setStone(u1.player.colour);//3
        pyramid.setStone(u1.player.colour);//2

        Assert.assertEquals(PlayerFieldColour.BLACK, pyramid.fields[0].playerColour);
        tm.calcInstaScore();
        Assert.assertEquals((Integer)8, u1.player.score);
        Assert.assertEquals((Integer)0, u2.player.score);

        pyramid.setStone(u2.player.colour);//4
        Assert.assertEquals(PlayerFieldColour.WHITE, pyramid.fields[4].playerColour);
        pyramid.setStone(u2.player.colour);//3
        pyramid.setStone(u2.player.colour);//2
        pyramid.setStone(u2.player.colour);//1
        tm.calcInstaScore();
        Assert.assertEquals((Integer)8, u1.player.score);
        Assert.assertEquals(PlayerFieldColour.WHITE, pyramid.fields[4].playerColour);

        Assert.assertEquals((Integer)10, u2.player.score);

        pyramid.setStone(u1.player.colour);//3
        pyramid.setStone(u1.player.colour);//2
        pyramid.setStone(u1.player.colour);//3
        pyramid.setStone(u1.player.colour);//1
        tm.calcInstaScore();
        Assert.assertEquals((Integer)17, u1.player.score);



        /*
        pyramid.setStone(u2.player.colour);//2
        Assert.assertEquals(1, pyramid.assessPoints(u2.player.colour));

        pyramid.setStone(u1.player.colour);//3
        pyramid.setStone(u1.player.colour);//4
        pyramid.setStone(u1.player.colour);//5
        Assert.assertEquals(11, pyramid.assessPoints(u2.player.colour));


        assertEquals(pyramid1.assessPoints(grey),0);

        pyramid1.setStone(brown);//0+2=2
        pyramid1.setStone(brown);//2+1=3

        assertEquals(PlayerFieldColour.BROWN, pyramid1.fields[0].playerColour);
        assertEquals(3, pyramid1.assessPoints(brown));
        assertEquals(2, pyramid1.stoneCounter);

        assertEquals(pyramid1.fields[2].playerColour, PlayerFieldColour.EMPTY);

        pyramid1.setStone(grey);//0+3=3
        pyramid1.setStone(grey);//3+2=5
        pyramid1.setStone(grey);//5+4=9

        assertEquals(pyramid1.assessPoints(grey),9);

        pyramid1.setStone(brown);//3+3=6
        pyramid1.setStone(brown);//6+2=8

        assertEquals(8, pyramid1.assessPoints(brown));
        assertEquals(7, pyramid1.stoneCounter);


        pyramid1.setStone(grey);//9+1=10
        pyramid1.setStone(grey);//10+3=13

        assertEquals(13, pyramid1.assessPoints(grey));
        assertEquals(9, pyramid1.stoneCounter);

        pyramid1.setStone(grey);//13+2=15

        assertEquals(15, pyramid1.assessPoints(grey));
        assertEquals(10, pyramid1.stoneCounter);
        */

    }
}
