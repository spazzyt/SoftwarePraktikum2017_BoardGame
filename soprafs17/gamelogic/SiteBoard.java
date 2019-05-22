package ch.uzh.ifi.seal.soprafs17.gamelogic;

import ch.uzh.ifi.seal.soprafs17.constant.PlayerFieldColour;
import ch.uzh.ifi.seal.soprafs17.entity.Move;

public abstract class SiteBoard implements Command, MoveZone {

    public String siteId;

    SiteBoard() {
        this.siteId = this.getClass().getSimpleName();
    }

    public boolean validateMove(Move m) {

        if( isCorrectZone(m) )
            return validateMoveImplementation(m);

        return false;
    }

    public boolean executeMove(Move m) {

        if( isCorrectZone(m) )
            return executeMoveImplementation(m);

        return false;
    }

    public abstract boolean executeMoveImplementation(Move m);

     public abstract boolean validateMoveImplementation(Move m);

     public abstract int assessPoints(PlayerFieldColour c);

}
