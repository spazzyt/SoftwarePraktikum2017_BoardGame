package ch.uzh.ifi.seal.soprafs17.entity;

import ch.uzh.ifi.seal.soprafs17.constant.PlayerFieldColour;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.ArrayList;

import static java.lang.Integer.max;

/**
 * Created by nairboon on 27.03.17.
 */
@Entity
public class ShipToken implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;

    @Column
    public int numFields;

    //todo: has to be assigned via round-card
    @Column
    public int fields[];

    @Column
    public int placedStones;

    // our ship id
    @Column
    public int shipId;


    public  ShipToken(int size, int id) {
        this.numFields = size;
        this.fields = new int[size];
        this.shipId = id;
    }



    public boolean setStone(int position, PlayerFieldColour c){
        if(fields[position] == PlayerFieldColour.EMPTY.ordinal()) {
            fields[position] = c.ordinal();
            placedStones++;
            return true;
        }
        return false;
    }


    public boolean movable(){
        if(placedStones >= max(numFields-1,1)){
            return true;
        }
        return false;
    }

    public boolean soonMovable(){
        if(placedStones >= max(numFields-2, 0)){
            return true;
        }
        return false;
    }

    public boolean updateStones(ArrayList<Integer> newWorldOrder) {
        if(newWorldOrder.size() != numFields)
            return false;

        for(int i= 0;i<numFields;i++) {
            int v = newWorldOrder.get(i);

            // invalid value
            if(v < PlayerFieldColour.EMPTY.ordinal() || v > PlayerFieldColour.GREY.ordinal())
                return false;

            fields[i] = v;
        }

        return true;
    }

    public ArrayList<Integer> asList() {
        ArrayList<Integer> r = new ArrayList<>();
        for(int i= 0;i<numFields;i++)
            r.add(fields[i]);

        return r;
    }
}
