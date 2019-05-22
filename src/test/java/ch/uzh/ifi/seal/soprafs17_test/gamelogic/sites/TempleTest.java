package ch.uzh.ifi.seal.soprafs17.gamelogic.sites;

import ch.uzh.ifi.seal.soprafs17.gamelogic.Temple;
import ch.uzh.ifi.seal.soprafs17.constant.PlayerFieldColour;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by Spasen on 07.04.17.
 */
/*
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
*/
public class TempleTest {


    //TODO validateMove and executeMove testing in TempleTest


    @Test
    public void testConstructorTemple()throws Exception{
        Temple temple2 = new Temple(2);
        Temple temple3 = new Temple(3);
        Temple temple4 = new Temple(4);

        assertNotNull(temple2);
        assertNotNull(temple3);
        assertNotNull(temple4);

        assertEquals(temple2.numPlayer,2);
        assertEquals(temple3.numPlayer,3);
        assertEquals(temple4.numPlayer,4);

        assertEquals(temple2.fields.length,4);
        assertEquals(temple3.fields.length,5);
        assertEquals(temple4.fields.length,5);

    }


    @Test
    public void testSetStone()throws Exception{
        PlayerFieldColour isBlack = PlayerFieldColour.BLACK;
        Temple temple2 = new Temple(2);

        assertEquals(temple2.getStoneCount(isBlack),0);

        temple2.setStone(isBlack);
        assertEquals(temple2.getStoneCount(isBlack),1);
        assertEquals(temple2.stoneCounter,1);

        PlayerFieldColour isWhite = PlayerFieldColour.WHITE;
        temple2.setStone(isWhite);
        assertEquals(temple2.getStoneCount(isWhite),1);
        assertEquals(temple2.getStoneCount(isBlack),1);
    }

    @Test
    public void testAssessPoints()throws Exception{
        PlayerFieldColour isBlack = PlayerFieldColour.BLACK;
        PlayerFieldColour isWhite = PlayerFieldColour.WHITE;
        Temple temple2 = new Temple(2);

        temple2.setStone(isBlack);
        temple2.setStone(isBlack);
        temple2.setStone(isBlack);
        temple2.setStone(isBlack);
        temple2.setStone(isBlack);

        assertEquals(temple2.assessPoints(isBlack),4);
        temple2.setStone(isBlack);
        temple2.setStone(isBlack);
        assertEquals(temple2.assessPoints(isBlack),4);

        temple2.setStone(isWhite);
        assertEquals(temple2.assessPoints(isBlack),3);

        Temple temple4 = new Temple(4);

        //6 stones in array of length 5 => 5 Points
        temple4.setStone(isWhite);
        temple4.setStone(isWhite);
        temple4.setStone(isWhite);
        temple4.setStone(isWhite);
        temple4.setStone(isWhite);

        temple4.setStone(isWhite);

        assertEquals(temple4.assessPoints(isWhite),5);
        temple4.setStone(isBlack);
        assertEquals(temple4.assessPoints(isWhite),4);
    }
}
