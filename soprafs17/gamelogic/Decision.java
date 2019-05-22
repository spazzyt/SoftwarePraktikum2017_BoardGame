package ch.uzh.ifi.seal.soprafs17.gamelogic;

import ch.uzh.ifi.seal.soprafs17.gamelogic.actions.Action;

import javax.persistence.Embeddable;
import javax.persistence.Transient;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by nairboon on 27.03.17.
 */
@Embeddable
public class Decision {

    // some blue market cards are of AND type, 2 actions combined
    public enum RelType { AND, OR };

    // normal decisions are of OR type, sail or place etc.
    @Transient
    public RelType relation = RelType.OR;

    @Transient
    public int nPicks = 1;

    @Transient
    public List<Action> actions = new ArrayList<>();

}
