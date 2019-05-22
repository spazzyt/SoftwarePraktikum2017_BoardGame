package ch.uzh.ifi.seal.soprafs17.gamelogic.actions;

import ch.uzh.ifi.seal.soprafs17.gamelogic.Factories.MoveFactory;
import ch.uzh.ifi.seal.soprafs17.gamelogic.Player;
import ch.uzh.ifi.seal.soprafs17.entity.Move;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Embeddable;
import javax.persistence.Transient;
import java.util.List;

/**
 * Created by nairboon on 01.04.17.
 */
@Embeddable
public class Action {


    public enum Type {
        Pickup, // Pickup a marketcard
        PlayCard, // Play a card
        Quarry, // Quarry to sled
        Sled,  // SledToShip
        Sail
    }

    public Type type;

    @JsonIgnore
    @Transient
    public MoveFactory factory;


    @Transient
    public List<Move> moves;

    protected Action() {

    }

    protected Action(Type type) {
        this.type = type;
    }

    public Integer generateValidMoves(Player p) {

        moves = factory.createMoves(p);
        return moves.size();

    }

    @Override
    public String toString() {
        return String.format("Action[Type: %s, Ms: %d]", type, moves.size());
    }
}
