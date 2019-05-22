package ch.uzh.ifi.seal.soprafs17.gamelogic.marketcards.bluecards;

import ch.uzh.ifi.seal.soprafs17.gamelogic.actions.Action;
import ch.uzh.ifi.seal.soprafs17.gamelogic.actions.QuarryToSledAction;
import ch.uzh.ifi.seal.soprafs17.gamelogic.actions.SledToShipAction;
import ch.uzh.ifi.seal.soprafs17.entity.Move;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;

/**
 * Created by nairboon on 09.05.17.
 */
public class HammerCard extends MultiMoveCard {

    @JsonIgnore
    private SledToShipAction sledToShipAction;

    public HammerCard() {
        this.comboMove = 2;
    }

    @Override
    public Action stepImplementation() {
        Action res = null;


        switch (comboMove) {

            case 2:
                // first move
                Move m = new Move("Quarry","Sled",3);
                res = new QuarryToSledAction();
                res.moves = new ArrayList<>();
                res.moves.add(m);

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