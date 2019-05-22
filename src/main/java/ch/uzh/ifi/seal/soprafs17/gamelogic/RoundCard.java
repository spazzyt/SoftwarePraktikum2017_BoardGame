package ch.uzh.ifi.seal.soprafs17.gamelogic;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by Spasen on 11.04.17.
 */
public class RoundCard {
/* RoundCards

for 2           for 3           for 4
3-3-2-2         4-3-3-2         3-3-3-2
4-3-3-1         4-3-2-1         4-4-2-1
3-3-2-1         3-3-2-2         4-4-3-2
4-2-2-1         4-2-2-1         4-3-3-2
4-3-2-1         4-3-2-2         4-3-2-2
3-2-2-1         3-3-3-2         4-4-2-2
4-3-2-2         4-4-2-1         4-3-3-3

*/

    private List<Integer[]> sizesTwoL   = new ArrayList<>();
    private List<Integer[]> sizesThreeL = new ArrayList<>();
    private List<Integer[]> sizesFourL  = new ArrayList<>();
    public  List<Integer[]> activeArray = new ArrayList<>();

    private Integer[][] sizesTwo   = {{3,3,2,2},{4,3,3,1},{3,3,2,1},{4,2,2,1},{4,3,2,1},{3,2,2,1},{4,3,2,2}};
    private Integer[][] sizesThree = {{4,3,3,2},{4,3,2,1},{3,3,2,2},{4,2,2,1},{4,3,2,2},{3,3,3,2},{4,4,2,1}};
    private Integer[][] sizesFour  = {{3,3,3,2},{4,4,2,1},{4,4,3,2},{4,3,3,2},{4,3,2,2},{4,4,2,2},{4,3,3,3}};


    //activeArray has shipsizes depending on how many players are playing
    public RoundCard(int numPlayers) {
        int randNum = ThreadLocalRandom.current().nextInt(0, 6 + 1);

        if(numPlayers == 2){
            sizesTwoL = new ArrayList<>(Arrays.asList(sizesTwo));
            sizesTwoL.remove(randNum);
            activeArray =sizesTwoL;
        }else if(numPlayers == 3){
            sizesThreeL = new ArrayList<>(Arrays.asList(sizesThree));
            sizesThreeL.remove(randNum);
            activeArray =sizesThreeL;
        }else if(numPlayers == 4){
            sizesFourL = new ArrayList<>(Arrays.asList(sizesFour));
            sizesFourL.remove(randNum);
            activeArray =sizesFourL;
        }
        shuffle();
    }


    private void shuffle(){
        long seed = System.nanoTime();
        Collections.shuffle(activeArray, new Random(seed));
    }

    List<Integer[]> getActiveArray(){
        return activeArray;
    }
}
