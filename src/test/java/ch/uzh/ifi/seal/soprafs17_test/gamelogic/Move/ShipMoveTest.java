package ch.uzh.ifi.seal.soprafs17.gamelogic.Move;

import ch.uzh.ifi.seal.soprafs17.gamelogic.*;
import ch.uzh.ifi.seal.soprafs17.constant.PlayerFieldColour;
import ch.uzh.ifi.seal.soprafs17.entity.Move;
import ch.uzh.ifi.seal.soprafs17.entity.ShipToken;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;

import static ch.uzh.ifi.seal.soprafs17.constant.PlayerFieldColour.*;

/**
 * Created by Spasen on 28.03.17.
 */


//info validate prüft wirklich wenns .pos ist was geprüft werden soll und valid prüft dies nicht...
    //nochmal anschauen
public class ShipMoveTest {

    public Integer size[] = {1,2,3,4};
    Player player = new Player(PlayerFieldColour.BLACK,2);
    /*
    * 1.Ship with size = 1
    * 2.Ship with size = 2
    * 3.Ship with size = 3
    * 4.Ship with size = 4
    * */

    @Test
    public void shipObeliskTest(){
        Move validMove_obelisk = new Move();
        Move validMove_temple = new Move();
        Move validMove_pyramid = new Move();
        Move validMove_burialChamber = new Move();

        Move invalidMove = new Move();

        DepartingHarbour harbour = new DepartingHarbour();
        harbour.setSizeOfShips(size);

        Obelisk obelisk = new Obelisk(4);
        Temple temple = new Temple(4);
        Pyramid pyramid = new Pyramid();
        BurialChamber burialChamber = new BurialChamber();

/*
* [Black]       (movable)
* [][]
* [][][]
* [][][][]
* */
//1 stone to first ship which has only 1 free space
        Assert.assertFalse(harbour.ships.get(0).movable());
        //sets a stone onto the first ship and the first position in the ship from EMPTY to BLACK
        Assert.assertTrue(harbour.ships.get(0).setStone(0, BLACK));
        //it's not possible to put the same color onto the ship(in the same position)
        Assert.assertFalse(harbour.ships.get(0).setStone(0, BLACK));


        //since the first ship has only one space for a stone, it is movable
        Assert.assertTrue(harbour.ships.get(0).movable());
        Assert.assertEquals( 1, harbour.ships.get(0).numFields);


        //all other ships must stay unmovable since their min-placed stones are not placed
        Assert.assertFalse(harbour.ships.get(1).movable());
        Assert.assertFalse(harbour.ships.get(2).movable());
        Assert.assertFalse(harbour.ships.get(3).movable());

//validateMove not set yet
        //fails because move has no "from" and no "to" set (that fits the "Obelisk)
        Assert.assertFalse(harbour.validateMove(validMove_obelisk));

        //fails because move has no "to" (that fits the "Obelisk")
        Assert.assertFalse(obelisk.validateMove(validMove_obelisk));

//setting valid values for the move
        validMove_obelisk.from ="DepartingHarbour";
        validMove_obelisk.to ="Obelisk";

        //if the first ship is movable then pos should be 0 and so on
        validMove_obelisk.pos = 0;

        //because Valid "from" and "to" are set
        Assert.assertTrue(harbour.validateMove(validMove_obelisk));
        Assert.assertTrue(obelisk.validateMove(validMove_obelisk));

        //because validateMove.toSite is not set, harbour and obelisk executeMove are false
        //Assert.assertEquals();
        Assert.assertFalse(harbour.executeMove(validMove_obelisk));
        Assert.assertFalse(obelisk.executeMove(validMove_obelisk));

        //usually done in turnManager
        validMove_obelisk.toSite = obelisk;

        //check goes through both assertTrue because validateMove.toSite is set
        Assert.assertTrue(harbour.executeMove(validMove_obelisk));
        Assert.assertTrue(obelisk.executeMove(validMove_obelisk));
        //after these two executions the first ship is not be in the harbour anymore

        //is false because the second ship is not movable
        validMove_obelisk.pos = 3; //validMove_obelisk.pos ist egal was es ist, es funktioniert immer
        Assert.assertFalse(harbour.validateMove(validMove_obelisk));



        invalidMove.from ="Departing Harbour";
        validMove_obelisk.to ="Obeliks";
        validMove_obelisk.pos = 42*42; //todo sollte nicht möglich sein

        //harbour.ships is hashmap
        Assert.assertNull(harbour.ships.get(0));

        //harbour.ships.site is 3 because the move for the 1 ship is executed
        Assert.assertEquals(3,harbour.ships.size());
        Assert.assertEquals( 2, harbour.ships.get(1).numFields);
        Assert.assertEquals( 2, harbour.ships.get(1).numFields);
        Assert.assertEquals( 2, harbour.ships.get(1).numFields);



/*
* [Black]       (movable)
* [][WHITE] + [WHITE][]  +  [WHITE][WHITE]    (movable)
* [][][] +
* [][][][]
* */
        //[][WHITE]
        Assert.assertTrue(harbour.ships.get(1).setStone(1, WHITE));
        Assert.assertEquals(1,harbour.ships.get(1).placedStones);

        //since the second ship has only one space for a stone, it is movable
        Assert.assertTrue(harbour.ships.get(1).movable());
        Assert.assertEquals( 2, harbour.ships.get(1).numFields);


        //all other ships must stay unmovable since their min-placed stones are not placed
        Assert.assertFalse(harbour.ships.get(2).movable());
        Assert.assertFalse(harbour.ships.get(3).movable());





/*
* [Black]       (movable)
* [][WHITE]     (movable) -> temple
* [][][]
* [][][][]
* */
        //ships.get(0) is null because it is executed
        Assert.assertNull(harbour.ships.get(0));
        Assert.assertNotNull(harbour.ships.get(1));

        //3 ships left in the harbour after execution
        Assert.assertEquals(3,harbour.ships.size());

        //second = ships.get(1) has 2 spaces
        Assert.assertEquals(2,harbour.ships.get(1).numFields);

        //first space in second ship is empty since the ship looks like this [][WHITE]
        Assert.assertEquals((int)0,harbour.ships.get(1).fields[0]);

        //PlayerFieldColor.White.ordinal() = 2
        Assert.assertEquals((int)2,harbour.ships.get(1).fields[1]);

        //second ship: [WHITE][WHITE]
        Assert.assertTrue(harbour.ships.get(1).setStone(0, WHITE));
        Assert.assertFalse(harbour.ships.get(1).setStone(0, WHITE));


        //of course the second ship must remain movable
        Assert.assertTrue(harbour.ships.get(1).movable());


/*
        //since the first ship has only one space for a stone, it is movable
        Assert.assertTrue(harbour.ships.get(1).movable());
        Assert.assertEquals( 2, harbour.ships.get(1).numFields);


        //all other ships must stay unmovable since their min-placed stones are not placed
        Assert.assertFalse(harbour.ships.get(2).movable());
        Assert.assertFalse(harbour.ships.get(3).movable());
        */

/*
*  null              (already moved)
* [WHITE][WHITE]     (movable)
* [][][]
* [GREY][GREY][GREY][GREY]
* */

        Assert.assertNull(harbour.ships.get(0));//executed
        Assert.assertNotNull(harbour.ships.get(1));//not executed

        //the second ship is movable
        Assert.assertTrue(harbour.ships.get(1).movable());
        Assert.assertEquals(2,harbour.ships.get(1).placedStones);



        //execute it does return false since validMove_temple is not set
        //Assert.assertFalse(harbour.val(validMove_temple));
        Assert.assertFalse(obelisk.executeMove(validMove_temple));
        Assert.assertFalse(temple.executeMove(validMove_temple)); //validMove_temple is not set yet
        Assert.assertFalse(harbour.executeMove(validMove_temple));



        Assert.assertNull(harbour.ships.get(0));//executed
        Assert.assertNotNull(harbour.ships.get(1));//not executed
        Assert.assertNotNull(harbour.ships.get(2));//not executed
        Assert.assertNotNull(harbour.ships.get(3));//not executed

        validMove_temple.from ="DepartingHarbour";
        validMove_temple.to ="Temple";
        validMove_temple.pos = 1;

        Assert.assertTrue(harbour.validateMove(validMove_temple));
        //harbour.executeMove(validMove_temple) is false because validMove_temple.toSite is not set
        Assert.assertFalse(harbour.executeMove(validMove_temple));
        Assert.assertFalse(obelisk.executeMove(validMove_temple));

        validMove_temple.toSite = temple;

        Assert.assertTrue(harbour.executeMove(validMove_temple));
        Assert.assertTrue(temple.executeMove(validMove_temple));

        Assert.assertNull(harbour.ships.get(0));//executed
        Assert.assertNull(harbour.ships.get(1));//executed
        Assert.assertNotNull(harbour.ships.get(2));//not executed
        Assert.assertNotNull(harbour.ships.get(3));//not executed

        Assert.assertTrue(harbour.ships.get(3).setStone(0, GREY));
        Assert.assertFalse(harbour.ships.get(3).movable());

        Assert.assertTrue(harbour.ships.get(3).setStone(1, GREY));
        Assert.assertFalse(harbour.ships.get(3).movable());

        Assert.assertTrue(harbour.ships.get(3).setStone(2, GREY));
        Assert.assertTrue(harbour.ships.get(3).movable());

        Assert.assertTrue(harbour.ships.get(3).setStone(3, GREY));
        Assert.assertTrue(harbour.ships.get(3).movable());



        //is going to fail because there is no 4
        //Assert.assertFalse(harbour.ships.get(3).setStone(4, GREY));

        Assert.assertFalse(harbour.ships.get(3).setStone(0, GREY));
        Assert.assertFalse(harbour.ships.get(3).setStone(1, GREY));
        Assert.assertFalse(harbour.ships.get(3).setStone(2, GREY));
        Assert.assertFalse(harbour.ships.get(3).setStone(3, GREY));

        validMove_pyramid.from ="DepartingHarbour";
        validMove_pyramid.to ="Pyramid";
        validMove_pyramid.pos = 3;

        Assert.assertTrue(harbour.validateMove(validMove_pyramid));
        //harbour.executeMove(validMove_temple) is false because validMove_temple.toSite is not set
        Assert.assertFalse(harbour.executeMove(validMove_pyramid));
        Assert.assertFalse(obelisk.executeMove(validMove_pyramid));

        validMove_pyramid.toSite = pyramid;

        Assert.assertTrue(harbour.validateMove(validMove_pyramid));
        Assert.assertFalse(harbour.executeMove(validMove_temple));// is false because validMove_temple.toSite is occupied
        Assert.assertTrue(harbour.executeMove(validMove_pyramid));
        Assert.assertFalse(obelisk.executeMove(validMove_pyramid));


//fürs dritte schiffli

        validMove_burialChamber.from = "DepartingHarbour";
        validMove_burialChamber.to = "BurialChamber";
        validMove_burialChamber.pos = 4; //should be false

        Assert.assertFalse(harbour.validateMove(validMove_burialChamber));
        Assert.assertTrue(burialChamber.validateMove(validMove_burialChamber));

        validMove_burialChamber.pos = 2; //should be false
        Assert.assertFalse(harbour.validateMove(validMove_burialChamber));
        //Assert.assertFalse(burialChamber.validateMove(validMove_burialChamber));

        validMove_burialChamber.pos = 3; //should be false
        Assert.assertFalse(harbour.validateMove(validMove_burialChamber));
        //Assert.assertFalse(burialChamber.validateMove(validMove_burialChamber));









    }



    @Test
    public void updateLever(){
        ShipToken ship1 = new ShipToken(4,1); // 4 x null
        ShipToken ship2 = new ShipToken(1,1); // 1 x null

        ship1.setStone(0,PlayerFieldColour.BLACK);
        ship1.setStone(1,PlayerFieldColour.BLACK);
        ship1.setStone(2,PlayerFieldColour.WHITE);
        ship1.setStone(3,PlayerFieldColour.WHITE);

        ArrayList<Integer> newWorldOrder = new ArrayList<>();
        //1-1-2-2
        newWorldOrder.add(2);
        newWorldOrder.add(2);
        newWorldOrder.add(1);
        newWorldOrder.add(1);

        Assert.assertTrue(ship1.updateStones(newWorldOrder));

        Assert.assertEquals(2, ship1.fields[0]);
        Assert.assertEquals(2, ship1.fields[1]);
        Assert.assertEquals(1, ship1.fields[2]);
        Assert.assertEquals(1, ship1.fields[3]);



    }
}
