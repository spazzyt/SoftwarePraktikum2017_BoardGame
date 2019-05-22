package ch.uzh.ifi.seal.soprafs17.gamelogic;

import ch.uzh.ifi.seal.soprafs17.constant.PlayerFieldColour;
import ch.uzh.ifi.seal.soprafs17.entity.Move;


public class Temple extends DestinationSite {

    public TempleField fields[];
    public int current = 0;
    //frontend wants this number, counts all stones on this site
    public int numPlayer;



    public Temple(int numberOfPlayers){
        this.numPlayer = numberOfPlayers;
        if(numberOfPlayers > 2 && numberOfPlayers < 5) {
            this.fields = new TempleField[5];

            for (int i = 0; i < 5; i++) {
                fields[i] = new TempleField();
            }
        }else if(numberOfPlayers == 2) {
            this.fields = new TempleField[4];

            for (int i = 0; i < 4; i++) {
                fields[i] = new TempleField();
            }
        }
    }

    public boolean validateMoveImplementation(Move m){

        // Es darf noch kein Schiff gedockt sein
        if(ship != null && m.from == "DepartingHarbour")
            return false;

        return true;

    }



    //todo needs work and excetions for 2-3-4 players
    public void setStone(PlayerFieldColour colour){
        //if fields is full
        if(current == fields.length){
            current = 0;
        }
        fields[current].playerColour = colour;
        current++;
        stoneCounter++;
    }



    public boolean executeMoveImplementation(Move m){
        //gets checked in dockableSite
        /*

        if(ship != null) {
            for (int i = 0; i < ship.fields.length; i++) {
                if(ship.fields[i] != PlayerFieldColour.EMPTY.ordinal()) {
                    setStone(PlayerFieldColour.values()[ship.fields[i]]);
                }
            }
            return true;
        }else{
            return false; //wenn ship == null
        }
        */
        return true;
    }


    public int getStoneCount(PlayerFieldColour c){
        int counter = 0;
        for( int i = 0; i < fields.length; i++){
            if(fields[i].playerColour == c){
                counter++;
            }
        }
        return counter;
    }


    @Override
    public int assessPoints(PlayerFieldColour c) {
        return getStoneCount(c);
    }

/*
    public int rankPlayer(PlayerFieldColour c){

        könnte man doch dasselbe wie in obelisk verwenden?
        -> wäre dann in destinationSite als normale methode
        List<ObeliskField>  res = Arrays.stream(fields).
                sorted((a, b)->Integer.compare(b.amount,a.amount))
                .collect(Collectors.toList());

        System.out.println(res);


        for(int i=0;i< numPlayer;i++) {
            System.out.println("Amount: "+ res.get(i).amount);
            if(res.get(i).playerColour == c)
                return i;
        }
        return -1;
    }
*/



}
