package ch.uzh.ifi.seal.soprafs17.gamelogic;

import ch.uzh.ifi.seal.soprafs17.constant.PlayerFieldColour;
import ch.uzh.ifi.seal.soprafs17.entity.Move;

/**
 * Created by Spasen on 07.04.17.
 */
public class BurialChamber extends DestinationSite{

    private int rows = 3;
    private int columns = 9;

    public int currentR = 0;
    public int currentC = 0;


    public BurialChamberField fields[][];

    public BurialChamber(){
        fields = new BurialChamberField[columns][rows]; //3 is column, 9 is row
        initFields();
    }

    public void initFields(){
        for(int column = 0; column < columns; column++){
            for(int row = 0; row < rows; row++){
                fields[column][row] = new BurialChamberField();
                //System.out.println(column + "");
            }
        }
    }


    public void setStone(PlayerFieldColour colour){
        if(currentR == rows){
            currentR = 0;
            currentC++;
        }
        if(currentC == columns){
            //sollte nie vorkommen
        }
        fields[currentC][currentR].playerColour = colour;
        currentR++;
        stoneCounter++;
    }

    @Override
    public boolean validateMoveImplementation(Move m) {

        System.out.println("BurialChamber: "+ m.to);

        // Es darf noch kein Schiff gedockt sein
        if(ship != null && m.from == "DepartingHarbour")//todo macht das sinn?
            return false;

        return true;
    }

    @Override
    public boolean executeMoveImplementation(Move m) {
        if(m.from.equals("Quarry")) {
            setStone(m.getUser().player.colour);
        }
        return true;
    }

    @Override
    public int assessPoints(PlayerFieldColour c) {
        if(c == PlayerFieldColour.EMPTY)
            return 0;
        return getPoints(c);
    }



    public int getPoints(PlayerFieldColour c){
        int[] scoreArray={0,1,3,6,10,15,17,19,21,23,25,27,29,31,33};
        int totalSgore = 0;
        int einPackman;
        //check if there is actually an occurance of c
        for(int column = 0; column < columns; column++){
            for(int row = 0; row < rows; row++){
                if(c == fields[column][row].playerColour && !fields[column][row].checked){

                    einPackman = packman(c, column, row);
                    totalSgore += scoreArray[einPackman];
                }
            }
        }
        return totalSgore;
    }



    public int packman(PlayerFieldColour c, int column, int row){

        if(column < 0 || column >= columns || row < 0 || row >= rows)
            return 0;

        if(fields[column][row].playerColour != c || fields[column][row].checked)
            return 0;

        fields[column][row].checked = true;
        return 1 +
                packman(c, column,row+1) +
                packman(c, column+1, row) +
                packman(c, column-1, row);
    }



}
