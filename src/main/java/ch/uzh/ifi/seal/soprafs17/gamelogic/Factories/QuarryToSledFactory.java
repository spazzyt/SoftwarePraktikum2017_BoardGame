package ch.uzh.ifi.seal.soprafs17.gamelogic.Factories;

import ch.uzh.ifi.seal.soprafs17.gamelogic.Player;
import ch.uzh.ifi.seal.soprafs17.entity.Move;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Spasen on 04.04.17.
 */
public class QuarryToSledFactory implements MoveFactory {


    private final Logger log = LoggerFactory.getLogger(QuarryToSledFactory.class);



    public QuarryToSledFactory() {

    }


/*
 *
 * quarry to sled
 * sled to ship
 * ship to site is created in ShipMoveFactory
 * Es fehlt noch:
 *          PickUnloadOrder
 *          QuarryToSite
question: for each action create super-method that
gets all the moves from quarry to sled, sled to ship,
ship to site? or just one result list? -> i think result is better
*/
    @Override
    public List<Move> createMoves(Player player) {
    /*   TODO QuarryToSite moves and PickUnloadOrder moves need to be created (after we implement the cards)
         dabei müssen die QuarryToSite und PickUnloadOrder eigene methoden sein, damit man diese
         separat abrufen kann
    */
        List<Move> results = new ArrayList<>();

        //quarry to sled
        Move quarryToSled = new Move();
        quarryToSled.from = "Quarry";
        quarryToSled.to = "Sled";
        quarryToSled.pos = player.sled.getSpaceCount();
        if(player.sled.validateMove(quarryToSled) && player.quarry.validateMove(quarryToSled) && quarryToSled.pos > 0){
            results.add(quarryToSled);
        }
        return results;
    }
}
