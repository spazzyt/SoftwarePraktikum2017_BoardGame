package ch.uzh.ifi.seal.soprafs17.gamelogic.sites;

import ch.uzh.ifi.seal.soprafs17.gamelogic.BurialChamber;
import ch.uzh.ifi.seal.soprafs17.constant.PlayerFieldColour;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by Spasen on 25.04.17.
 */
public class BurialChamberTest {


    @Test
    public void construct(){

        BurialChamber burialChamber = new BurialChamber();
        Assert.assertEquals(PlayerFieldColour.EMPTY, burialChamber.fields[0][0].playerColour);
        Assert.assertEquals(PlayerFieldColour.EMPTY, burialChamber.fields[8][2].playerColour);

        jackThePoints(burialChamber,0,0,0,0);



    }

    @Test
    public void kleinsteinEinstein(){
        BurialChamber burialChamber = new BurialChamber();
        burialChamber.setStone(PlayerFieldColour.BLACK);
        Assert.assertEquals(1,burialChamber.stoneCounter);
        jackThePoints(burialChamber, 1, 0, 0, 0);
    }

    @Test
    public void dreiStein(){
        BurialChamber burialChamber = new BurialChamber();
        burialChamber.setStone(PlayerFieldColour.BLACK);
        burialChamber.setStone(PlayerFieldColour.BLACK);
        burialChamber.setStone(PlayerFieldColour.BLACK);
        jackThePoints(burialChamber, 6, 0, 0, 0);
    }


    /*
    * [1][1][1][1][][][][][]
    * [2][2][2][][][][][][]
    * [4][4][3][][][][][][]
    * */
    @Test
    public void rainbowOne(){
        BurialChamber burialChamber = new BurialChamber();
        burialChamber.setStone(PlayerFieldColour.BLACK);
        burialChamber.setStone(PlayerFieldColour.WHITE);
        burialChamber.setStone(PlayerFieldColour.GREY);
        burialChamber.setStone(PlayerFieldColour.BLACK);
        burialChamber.setStone(PlayerFieldColour.WHITE);
        burialChamber.setStone(PlayerFieldColour.GREY);
        burialChamber.setStone(PlayerFieldColour.BLACK);
        burialChamber.setStone(PlayerFieldColour.WHITE);
        burialChamber.setStone(PlayerFieldColour.BROWN);
        burialChamber.setStone(PlayerFieldColour.BLACK);
        jackThePoints(burialChamber, 10, 6, 1, 3);
    }


    /*
    * [2][2][4][4][][][][][]
    * [3][2][4][1][][][][][]
    * [2][2][2][1][][][][][]
    * */
    @Test
    public void rainbowTwo(){ //snake
        BurialChamber burialChamber = new BurialChamber();
        burialChamber.setStone(PlayerFieldColour.WHITE);
        burialChamber.setStone(PlayerFieldColour.BROWN);
        burialChamber.setStone(PlayerFieldColour.WHITE);

        burialChamber.setStone(PlayerFieldColour.WHITE);
        burialChamber.setStone(PlayerFieldColour.WHITE);
        burialChamber.setStone(PlayerFieldColour.WHITE);

        burialChamber.setStone(PlayerFieldColour.GREY);
        burialChamber.setStone(PlayerFieldColour.GREY);
        burialChamber.setStone(PlayerFieldColour.WHITE);

        burialChamber.setStone(PlayerFieldColour.GREY);
        burialChamber.setStone(PlayerFieldColour.BLACK);
        burialChamber.setStone(PlayerFieldColour.BLACK);
        jackThePoints(burialChamber, 3, 17, 1, 6);
    }

    /*
    * [2][1][1][1][][][][][]
    * [1][4][4][][][][][][]
    * [1][2][2][][][][][][]
    * */
    @Test
    public void rainbowThree(){ //snake
        BurialChamber burialChamber = new BurialChamber();
        burialChamber.setStone(PlayerFieldColour.WHITE);
        burialChamber.setStone(PlayerFieldColour.BLACK);
        burialChamber.setStone(PlayerFieldColour.BLACK);

        burialChamber.setStone(PlayerFieldColour.BLACK);
        burialChamber.setStone(PlayerFieldColour.GREY);
        burialChamber.setStone(PlayerFieldColour.WHITE);

        burialChamber.setStone(PlayerFieldColour.BLACK);
        burialChamber.setStone(PlayerFieldColour.GREY);
        burialChamber.setStone(PlayerFieldColour.WHITE);

        burialChamber.setStone(PlayerFieldColour.BLACK);
        jackThePoints(burialChamber, 9, 4, 0, 3);
    }


    /*
        * [2][2][1][][][][][][]
        * [2][2][2][][][][][][]
        * [2][2][][][][][][][]
        * */
    @Test
    public void rainbowFour(){ //snake
        BurialChamber burialChamber = new BurialChamber();
        burialChamber.setStone(PlayerFieldColour.WHITE);
        burialChamber.setStone(PlayerFieldColour.WHITE);
        burialChamber.setStone(PlayerFieldColour.WHITE);

        burialChamber.setStone(PlayerFieldColour.WHITE);
        burialChamber.setStone(PlayerFieldColour.WHITE);
        burialChamber.setStone(PlayerFieldColour.WHITE);

        burialChamber.setStone(PlayerFieldColour.BLACK);
        burialChamber.setStone(PlayerFieldColour.WHITE);
        jackThePoints(burialChamber, 1, 19, 0, 0);
    }


    private void jackThePoints(BurialChamber bc, int black, int white, int brown, int grey){
        Assert.assertEquals(0, bc.assessPoints(PlayerFieldColour.EMPTY));
        Assert.assertEquals(black, bc.assessPoints(PlayerFieldColour.BLACK));
        Assert.assertEquals(white, bc.assessPoints(PlayerFieldColour.WHITE));
        Assert.assertEquals(brown, bc.assessPoints(PlayerFieldColour.BROWN));
        Assert.assertEquals(grey, bc.assessPoints(PlayerFieldColour.GREY));
    }

}
