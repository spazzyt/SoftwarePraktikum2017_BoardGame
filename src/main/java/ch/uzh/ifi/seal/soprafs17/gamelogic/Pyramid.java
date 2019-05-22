package ch.uzh.ifi.seal.soprafs17.gamelogic;

import ch.uzh.ifi.seal.soprafs17.constant.PlayerFieldColour;
import ch.uzh.ifi.seal.soprafs17.entity.Move;

import java.util.HashMap;

/**
 * Created by Spasen on 06.04.17.
 */
public class Pyramid extends DestinationSite{

    public PyramidField fields[];
    public int current = 0;
    public HashMap<PlayerFieldColour,Integer> lastCurrent;
    public int pointsArray[] = {2,1,3,2,4,3,2,1,3,2,3,1,3,4};


    public Pyramid(){
        initArray();
        lastCurrent = new HashMap<>();
        lastCurrent.put(PlayerFieldColour.EMPTY,0 );
        lastCurrent.put(PlayerFieldColour.BLACK,0 );
        lastCurrent.put(PlayerFieldColour.WHITE,0 );
        lastCurrent.put(PlayerFieldColour.BROWN,0 );
        lastCurrent.put(PlayerFieldColour.GREY,0  );

    }

    public void initArray(){
        fields = new PyramidField[120];
        for(int i = 0; i<14;i++){
            fields[i] = new PyramidField();
            fields[i].points = pointsArray[i];
            //System.out.println(i + ": " + fields[i].points);
        }
        for(int i = 14; i<fields.length;i++){
            fields[i] = new PyramidField();
            fields[i].points = 1;
            //System.out.println(i + ": " + fields[i].points);
        }
    }


    public boolean validateMoveImplementation(Move m){
        System.out.println("Pyramid: "+ m.to);
        if(ship != null && m.from == "DepartingHarbour")
            return false;
        return true;
    }

    public void setStone(PlayerFieldColour colour){
        fields[current].playerColour = colour;
        current++;
        stoneCounter++;
    }


    public boolean executeMoveImplementation(Move m){
        //setStone gets called from dockableSite
        if(m.from.equals("Quarry")) {
            setStone(m.getUser().player.colour);
        }
        return true;
    }

/*
public int assessPoints(PlayerFieldColour c) {
        int playerPoints = 0;
        for(int i = 0; i < fields.length; i++){
            if(fields[i].playerColour == c){
                playerPoints += fields[i].points;
            }
        }
        return playerPoints;
    }
*/
    public int assessPoints(PlayerFieldColour c) {
        int playerPoints = 0;
        for(int i = lastCurrent.get(c); i < fields.length; i++){
            if(fields[i].playerColour == c){
                playerPoints += fields[i].points;
            }
        }
        lastCurrent.put(c, current);
        return playerPoints;
    }
}
