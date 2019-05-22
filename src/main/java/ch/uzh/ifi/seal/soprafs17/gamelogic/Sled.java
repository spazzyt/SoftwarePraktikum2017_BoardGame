package ch.uzh.ifi.seal.soprafs17.gamelogic;

import ch.uzh.ifi.seal.soprafs17.constant.PlayerFieldColour;
import ch.uzh.ifi.seal.soprafs17.entity.Move;

import javax.persistence.Embeddable;
import javax.persistence.Transient;

import static java.lang.Integer.min;

/**
 * Created by Spasen on 03.04.17.
 */

@Embeddable
public class Sled extends OriginAndDepartingSite{

    public Integer sledLoad = 0;
    public Integer maxSled = 5;
    @Transient
    public PlayerFieldColour c;

    @Transient
    public Quarry quarry;

    @Transient
    public PlayerFieldColour sled[];

    // default constr for hibernate
    public Sled() {}

    //initializes sled with right colour and right amount of starting stones
    public Sled(PlayerFieldColour colour, int playerOrder){
        c = colour;
        sled = new PlayerFieldColour[maxSled];
        quarry = new Quarry(colour);
        for(int i = 0; i < maxSled; i++){
            sled[i] = PlayerFieldColour.EMPTY;
            if(playerOrder > i) {
                sled[i] = colour;
                sledLoad++;
                quarry.stonesLeft--;
            }
        }
    }

    // necessary for the frontend
    public int getSpaceCount(){
            return min(3, maxSled - sledLoad);
    }


    @Override
    public boolean validateMoveImplementation(Move m) {
        if(m.from.equals("Sled")){
            if (sledLoad - 1 >= 0) {
                return true;
            }
        }else if(m.to.equals("Sled")){
            if(sledLoad+m.pos <=maxSled){
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean executeMoveImplementation(Move m) {
        if(m.from.equals("Sled")) {
            // remove the m.pos amount of stones

            this.sledLoad -= 1;
            return true;
        }else if(m.to.equals("Sled")){
            this.sledLoad +=m.pos;
            return true;
        }
        return false;
    }

    @Override
    public int assessPoints(PlayerFieldColour c) {
        return 0;
    }

}
