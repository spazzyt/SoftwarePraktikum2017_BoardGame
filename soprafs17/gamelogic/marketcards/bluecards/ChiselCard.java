package ch.uzh.ifi.seal.soprafs17.gamelogic.marketcards.bluecards;

import ch.uzh.ifi.seal.soprafs17.gamelogic.actions.Action;
import ch.uzh.ifi.seal.soprafs17.gamelogic.actions.SledToShipAction;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Created by nairboon on 09.05.17.
 */
public class ChiselCard extends MultiMoveCard {

    @JsonIgnore
    private SledToShipAction sledToShipAction;
    
    public ChiselCard() {
        this.comboMove = 2;
    }
    
    @Override
    public Action stepImplementation() {
        Action res = null;
        
              /*
             When you get this market card, it remains
             face up in front of you until you use it. In a
             later turn, you can perform the following
             action a single time:
             Place 2 stones on 1 ship or 1 stone on each of 2 ships.
             After play, the card is placed on the discard pile.
             If you have not used the card by the end of the game, you
             will get 1 point for it.
             -----

             Decision should look like:
                Sled to DepartingHarbour with 2 stones
                OR
                2x Sled to DepartingHarbour with 1 stone
            */
              
        /*
        Integer res = sledToShipAction.generateValidMoves(p);
        if(res > 1) {
            p.decision.actions = new ArrayList<>();
            p.decision.actions.add(sledToShipAction);
            p.decision.nPicks = 2;
            return true;
        }
         */
        
        switch (comboMove) {
            
            case 2:
                // first move
                sledToShipAction.generateValidMoves(player);
                res = sledToShipAction;
                break;
            case 1:
                // second one
                sledToShipAction.generateValidMoves(player);
                res = sledToShipAction;
                break;
        }
        
        return res;
    }

    public void setSledToShipAction(SledToShipAction sledToShipAction) {
        this.sledToShipAction = sledToShipAction;
    }
}
