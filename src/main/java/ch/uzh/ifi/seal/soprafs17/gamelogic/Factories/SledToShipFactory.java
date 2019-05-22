package ch.uzh.ifi.seal.soprafs17.gamelogic.Factories;

import ch.uzh.ifi.seal.soprafs17.gamelogic.DepartingHarbour;
import ch.uzh.ifi.seal.soprafs17.gamelogic.Player;
import ch.uzh.ifi.seal.soprafs17.constant.PlayerFieldColour;
import ch.uzh.ifi.seal.soprafs17.entity.Move;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Spasen on 13.04.17.
 */
public class SledToShipFactory implements MoveFactory{

    private final Logger log = LoggerFactory.getLogger(SledToShipFactory.class);
    public Player player;
    public DepartingHarbour harbour;

    public SledToShipFactory(DepartingHarbour dh) throws MoveFactoryException {
        if(dh == null) {
            log.error("departingHarbour is null");
            throw new MoveFactoryException();
        }
            harbour = dh;

    }


    /*
     * Es fehlt noch:
     *          PickUnloadOrder
     *          QuarryToSite
    */
    @Override
    public List<Move> createMoves(Player player) {
/*   TODO QuarryToSite moves and PickUnloadOrder moves need to be created (after we implement the cards)
     dabei müssen die QuarryToSite und PickUnloadOrder eigene methoden sein, damit man diese
     separat abrufen kann
*/
        List<Move> results = new ArrayList<>();


        if(player.sled.sledLoad>0) {
            //sled to ship
            for (int i : harbour.ships.keySet()) {// through every ship
                int sfields[] = harbour.ships.get(i).fields;
                Move nMove = new Move();
                nMove.ShipID = i;
                nMove.from = "Sled";
                for (int j = 0; j < sfields.length; j++) {
                    nMove.pos = j;
                    if (sfields[j] == PlayerFieldColour.EMPTY.ordinal()) {
                        Move sledToShip = new Move();
                        sledToShip.from = nMove.from;
                        sledToShip.ShipID = nMove.ShipID;
                        sledToShip.pos = nMove.pos;
                        sledToShip.to = harbour.siteId;
                        results.add(sledToShip);
                    }
                }
            }
        }
        return results;
    }

    public List<Move> createMovesSailCard(Player player){
        List<Move> results = new ArrayList<>();
        if(player.sled.sledLoad > 0){
            for(int i : harbour.ships.keySet()){
                int sfields[] = harbour.ships.get(i).fields;

                for(int j = 0; j < sfields.length; j++) {
                    Move m = new Move();
                    m.ShipID = i;
                    m.from = "Sled";
                    m.pos = j;
                    m.to = harbour.siteId;

                    // there must be space
                    if(sfields[j] == PlayerFieldColour.EMPTY.ordinal()
                            // and soon shipable
                            && harbour.ships.get(i).soonMovable() ){
                        results.add(m);
                    }
                }
            }
        }
        return results;
    }
}