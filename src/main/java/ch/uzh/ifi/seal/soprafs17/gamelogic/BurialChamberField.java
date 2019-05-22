package ch.uzh.ifi.seal.soprafs17.gamelogic;

import ch.uzh.ifi.seal.soprafs17.constant.PlayerFieldColour;

/**
 * Created by Spasen on 07.04.17.
 */
public class BurialChamberField {

    public PlayerFieldColour playerColour;
    public boolean checked = false;

    public BurialChamberField(){
        playerColour = PlayerFieldColour.EMPTY;
    }



}
