package ch.uzh.ifi.seal.soprafs17.gamelogic;

import ch.uzh.ifi.seal.soprafs17.constant.PlayerFieldColour;
import ch.uzh.ifi.seal.soprafs17.entity.ShipToken;

/**
 * Created by nairboon on 25.04.17.
 */
public abstract class DockableSite extends SiteBoard {


    public ShipToken ship;

    public void dockShip(ShipToken s) {
        ship = s;
        onShipDocked();
    }

    public void onShipDocked() {
        for (int i = 0; i < ship.fields.length; i++) {
            if(ship.fields[i] != PlayerFieldColour.EMPTY.ordinal()) {
                setStone(PlayerFieldColour.values()[ship.fields[i]]);
            }
        }
    }

    public void setStone(PlayerFieldColour c) { }

    public void clearShip() {
        if(ship != null)
            ship = null;
    }

}
