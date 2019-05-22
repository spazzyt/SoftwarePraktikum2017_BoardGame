package ch.uzh.ifi.seal.soprafs17.gamelogic;


import ch.uzh.ifi.seal.soprafs17.entity.Move;

public abstract class OriginAndDepartingSite extends DockableSite {

    @Override
    public boolean isCorrectZone(Move m) {
        //checks if OriginAndDepartingSite == m.from || OriginAndDepartingSite == m.to
        if( this.getClass().getSimpleName().equals(m.from) || this.getClass().getSimpleName().equals(m.to))
            return true;

        return false;
    }

    /*
    @Override
    public int assessPoints(PlayerFieldColour c) {
        return 0;
    }
    */
}
