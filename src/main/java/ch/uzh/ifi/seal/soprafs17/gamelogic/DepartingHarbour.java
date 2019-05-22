package ch.uzh.ifi.seal.soprafs17.gamelogic;

import ch.uzh.ifi.seal.soprafs17.constant.PlayerFieldColour;
import ch.uzh.ifi.seal.soprafs17.entity.Move;
import ch.uzh.ifi.seal.soprafs17.entity.ShipToken;

import java.util.HashMap;


public class DepartingHarbour extends OriginAndDepartingSite implements Command {
// todo has no validateMove method (only gets it from OriginAndDepartingSite)


    public HashMap<Integer,ShipToken> ships;


    //int sizes[] = {1,2,3,4};
    


    //parameter would be a roundcard
    public void setSizeOfShips(Integer sizes[]) {
        ships = new HashMap<>();




        //4 neue
        for(int i = 0; i < 4; i++){
            ships.put(i, new ShipToken(sizes[i], i));
        }
    }



    public boolean validateMoveImplementation(Move m) {
        //schiff segeln
        if(m.from.equals("DepartingHarbour")) {
            //todo sweet explanation for next line needed (when is -1 < m.pos not true??)
            if (ships.containsKey(m.pos) && ships.get(m.pos).movable())
                return true;
            else
                return false;
        //stein auf schiff schmeissen
        }else if(m.to.equals("DepartingHarbour")) {
            // is that ship still here
            if(ships.containsKey(m.ShipID))
                return true;
           // for(ShipToken s : ships)
               // if(m.ShipID == s.)
        }
        return false;
    }

    //TODO: check for ship that is not full

    public boolean executeMoveImplementation(Move m) {

        // Lever card
        if(m.from.equals("DepartingHarbour") && m.to.equals("DepartingHarbour")) {
            if (!ships.containsKey(m.pos))
                return false;

            ShipToken ship = ships.get(m.pos);

            ship.updateStones(m.LeverStones);

            return true;
        }
        //schiff segeln
        else if(m.from.equals("DepartingHarbour")) {
            if (!ships.containsKey(m.pos) || m.toSite == null)
                return false;

            ShipToken ship = ships.get(m.pos);

            ((DockableSite) m.toSite).dockShip(ship);
            ships.remove(m.pos);

            return true;
        } else if(m.to.equals("DepartingHarbour")) {

            ShipToken s = ships.get(m.ShipID);
            if(s.setStone(m.pos,m.getUser().player.colour))
                return true;
        }

        return false;
    }

    @Override
    public int assessPoints(PlayerFieldColour c) {
        return 0;
    }


    public Integer gibSoonMovable() {
        int counter = 0;
        for(int i = 0; i < ships.size(); i++){
            if(ships.get(i).soonMovable()) {
                counter++;
            }
        }
        return counter;
    }
}
