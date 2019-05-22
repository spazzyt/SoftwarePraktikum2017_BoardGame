package ch.uzh.ifi.seal.soprafs17.gamelogic.marketcards.bluecards;

import ch.uzh.ifi.seal.soprafs17.gamelogic.actions.Action;
import ch.uzh.ifi.seal.soprafs17.gamelogic.actions.LeverAction;
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
public class LeverCard extends MultiMoveCard  {

    private final Logger log = LoggerFactory.getLogger(LeverCard.class);

    @JsonIgnore
    private SailAction sailAction;

    @JsonIgnore
    private SledToShipAction sledToShipAction;

    @JsonIgnore
    private LeverAction leverAction;



    public LeverCard() {
        this.comboMove = 2;
    }

    @Override
    protected Action stepImplementation() {
        Action res = null;

        switch (comboMove) {

            // reorder stones
            case 2:
                leverAction.generateValidMoves(player);
                res = leverAction;
                break;

            // ship it
            case 1:

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

    public void setSailAction(SailAction sailAction) {
        this.sailAction = sailAction;
    }

    public void setLeverAction(LeverAction leverAction) {
        this.leverAction = leverAction;
    }
}
