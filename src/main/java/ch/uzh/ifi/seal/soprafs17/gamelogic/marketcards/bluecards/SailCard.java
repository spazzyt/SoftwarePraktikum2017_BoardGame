package ch.uzh.ifi.seal.soprafs17.gamelogic.marketcards.bluecards;

import ch.uzh.ifi.seal.soprafs17.gamelogic.actions.Action;
import ch.uzh.ifi.seal.soprafs17.gamelogic.actions.SailAction;
import ch.uzh.ifi.seal.soprafs17.gamelogic.actions.SledToShipAction;
import ch.uzh.ifi.seal.soprafs17.entity.Move;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nairboon on 09.05.17.
 */
public class SailCard extends MultiMoveCard  {

    private final Logger log = LoggerFactory.getLogger(SailCard.class);

    @JsonIgnore
    private SailAction sailAction;

    @JsonIgnore
    private SledToShipAction sledToShipAction;


    public SailCard() {
        this.comboMove = 2;
    }

    @Override
    protected Action stepImplementation() {
        Action res = null;


        switch (comboMove) {

            case 2:
                // first place stone on a ship
                sledToShipAction.moves = sledToShipAction.factory.createMovesSailCard(player);
                res = sledToShipAction;
                break;
            case 1:
                // next sail that ship
                // which ship did we set the stone on?
              /*  if(!lastMove.to.equals("DepartingHarbour")) {
                    log.error("previous move wasn't to the harbour???");
                    return null;
                } */

                sailAction.generateValidMoves(player);

                // only for the ship from step 1.
                List<Move> filtered = new ArrayList<>();
                for(Move m : sailAction.moves)
                    if(m.pos.equals(lastMove.ShipID))
                        filtered.add(m);

                sailAction.moves = filtered;

                res = sailAction;
                break;
        }

        return res;
    }

    public void setSledToShipAction(SledToShipAction sledToShipAction) {
        this.sledToShipAction = sledToShipAction;
    }

    public void setSailAction(SailAction sailAction) {
        this.sailAction = sailAction;
    }
}
