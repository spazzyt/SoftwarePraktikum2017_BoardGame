package ch.uzh.ifi.seal.soprafs17.gamelogic;

import ch.uzh.ifi.seal.soprafs17.entity.Move;

/**
 * Created by nairboon on 28.03.17.
 */

public abstract class DestinationSite extends DockableSite {

    public int stoneCounter = 0;


    public int getGreenCount(){
        return stoneCounter/3;
    }



    @Override
    public boolean isCorrectZone(Move m) {
        if( this.getClass().getSimpleName().equals(m.to) )
            return true;

        return false;
    }

}