package ch.uzh.ifi.seal.soprafs17.gamelogic;

import ch.uzh.ifi.seal.soprafs17.constant.PlayerFieldColour;
import ch.uzh.ifi.seal.soprafs17.entity.Move;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


public class Obelisk extends DestinationSite {

    public ObeliskField[] fields;
    int[][] goreArray = {{10,1}, {12,6,1},{15,10,5,1}};
    int numPlayer;



    public Obelisk( int numberOfPlayers) {

        this.numPlayer = numberOfPlayers;
        this.fields = new ObeliskField[numberOfPlayers];
        for(int i = 0; i<fields.length;i++) {
            fields[i] = new ObeliskField(PlayerFieldColour.values()[i+1]);
        }
    }




    //sollte doch in DestinationSite sein?
    @Override
    public boolean validateMoveImplementation(Move m) {

        // Es darf noch kein Schiff gedockt sein
        if(ship != null && m.from == "DepartingHarbour")//todo macht das sinn?
            return false;

        return true;
    }


    @Override
    public void setStone(PlayerFieldColour colour){
        for(int i = 0; i<fields.length; i++){
            if(fields[i].playerColour == colour){
                fields[i].amount++;
                stoneCounter++;
            }
        }
    }

    //sollte doch in DestinationSite sein
    @Override
    public boolean executeMoveImplementation(Move m) {
	    // origin site executes target.dock(ship)
	    // ship != null
       if(ship != null) {
            for (int i = 0; i < ship.fields.length; i++) {
                if(ship.fields[i] != PlayerFieldColour.EMPTY.ordinal()) {
                    setStone(PlayerFieldColour.values()[ship.fields[i]]);
                }
            }
            return true;

        }else if(m.from.equals("Quarry")) {
            setStone(m.getUser().player.colour);
            return true;
        }

        return false;
    }

    @Override
    public int assessPoints(PlayerFieldColour c) {
        return goreArray[numPlayer-2][rankPlayer(c)];
    }

    //TODO: überlegen ob dies nicht in die parentklasse passt
    public int rankPlayer(PlayerFieldColour c) {

	    List<ObeliskField>  res = Arrays.stream(fields).
                sorted((a, b)->Integer.compare(b.amount,a.amount))
                .collect(Collectors.toList());



        for(int i=0;i< numPlayer;i++) {
            if(res.get(i).playerColour == c)
                return i;
        }

	    return -1;
    }




}
