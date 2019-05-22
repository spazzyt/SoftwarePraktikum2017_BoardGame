package ch.uzh.ifi.seal.soprafs17.gamelogic;

import ch.uzh.ifi.seal.soprafs17.constant.PlayerFieldColour;
import ch.uzh.ifi.seal.soprafs17.entity.Move;

import javax.persistence.Embeddable;
import javax.persistence.Transient;

/**
 * Created by Spasen on 03.04.17.
 */


@Embeddable
public class Quarry extends OriginAndDepartingSite{

    public Integer stonesLeft;

    @Transient
    public PlayerFieldColour playerFieldColor;

    // default constr for hibernate
    public Quarry() {}
    public Quarry(PlayerFieldColour colour){
        playerFieldColor = colour;
        stonesLeft = 29;
    }


    public boolean executeMoveImplementation(Move m){

        if(m.from.equals("Quarry")){
            stonesLeft-=m.pos;
            return true;
        }
        return false;
    }


    public boolean validateMoveImplementation(Move move){
        if(stonesLeft-move.pos >= 0){
            return true;
        }
        return false;
    }

    @Override
    public int assessPoints(PlayerFieldColour c) {
        return 0;
    }
}
