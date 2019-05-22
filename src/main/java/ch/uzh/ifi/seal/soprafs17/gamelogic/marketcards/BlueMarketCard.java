package ch.uzh.ifi.seal.soprafs17.gamelogic.marketcards;

import ch.uzh.ifi.seal.soprafs17.gamelogic.actions.LeverAction;
import ch.uzh.ifi.seal.soprafs17.gamelogic.actions.SailAction;
import ch.uzh.ifi.seal.soprafs17.gamelogic.actions.SledToShipAction;
import ch.uzh.ifi.seal.soprafs17.gamelogic.Market;
import ch.uzh.ifi.seal.soprafs17.gamelogic.marketcards.bluecards.ChiselCard;
import ch.uzh.ifi.seal.soprafs17.gamelogic.marketcards.bluecards.HammerCard;
import ch.uzh.ifi.seal.soprafs17.gamelogic.marketcards.bluecards.LeverCard;
import ch.uzh.ifi.seal.soprafs17.gamelogic.marketcards.bluecards.SailCard;
import ch.uzh.ifi.seal.soprafs17.gamelogic.Player;
import ch.uzh.ifi.seal.soprafs17.entity.Move;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Spasen on 19.04.17.
 */
public class BlueMarketCard  extends MarketCard {



    public enum BlueType { Lever, Hammer, Sail, Chisel};

    public List<Move> moves = new ArrayList<>();

    public BlueType myType;

    @JsonIgnore
    private LeverAction leverAction;

    @JsonIgnore
    private SailAction sailAction;

    @JsonIgnore
    private SledToShipAction sledToShipAction;

    @JsonIgnore
    public Market market;

    public int comboMove = 0;

    public BlueMarketCard(BlueType type, int card_id, SailAction sa, SledToShipAction ssa) {
        super(MarketCard.Type.Blue, card_id);
        myType = type;


        sailAction = sa;
        sledToShipAction = ssa;
        leverAction = new LeverAction(sailAction.factory.harbour);

        switch (type) {

            case Hammer:
                comboMove = 1;
                break;

            case Lever:
                comboMove = 1;
                break;

            case Sail:
                comboMove = 1;
                break;

            case Chisel:
                comboMove = 2;
                break;
        }
    }

/*
-Hole dir 3 Steine aus dem Steinbruch und lege sie auf dein Vorrats-Plättchen.
-Platziere anschließend 1 Stein von deinem Vorrats-Plättchen auf 1 Boot.
-Die Karte kommt anschließend auf den Ablagestapel. Falls du die Karte bis
 zum Spielende nicht eingesetzt hast, bekommst du dafür noch 1 Punkt.
* */

    public boolean isPlayable(Player p) {
            /*
            Hole dir 3 Steine aus dem Steinbruch und lege sie auf dein Vorrats-Plättchen.
            Platziere anschließend 1 Stein von deinem Vorrats-Plätt- chen auf 1 Boot.
            */
        if(myType == BlueType.Hammer) {
            List<Move> stsmoves = sledToShipAction.factory.createMoves(p);
            if (p.quarry.stonesLeft >= 3 && p.sled.sledLoad < 3
                    && !stsmoves.isEmpty()) //check für einen freien schiffplatz
                return true;

        } else if(myType == BlueType.Lever) {
            Integer moveCount = sailAction.generateValidMoves(p);
            // there is atleast one ship sailable
            if(moveCount > 0)
                return true;

        } else if(myType == BlueType.Chisel) {
            moves = sledToShipAction.factory.createMoves(p);
            if (p.quarry.stonesLeft >= 2 && moves.size()>1) {
                return true;
            }

        } else if(myType == BlueType.Sail){
             moves = sledToShipAction.factory.createMovesSailCard(p);
            if(moves.size()>0)
                return true;
        }
        return false;
    }

    public boolean playCard(Move move) {

        Player p = move.getUser().player;


        if(myType == BlueType.Hammer) {

            p.playingCard = makeHammerCard(p);
            return true;

        } else if(myType == BlueType.Lever) {

            p.playingCard = makeLeverCard(p);
            return true;



            // Platziere 1 Stein auf 1 Boot und fahre dieses Boot zu einem Ort = Sail-Karte
        }else if(myType == BlueType.Sail) {
            /*
                Protocol:
                    PLAYCARD move contains ShipID on which the stone should be placed
                    - generate sail move
             */

            p.playingCard = makeSailCard(p);
            return true;

        } else if(myType == BlueType.Chisel) {


            // set the current card as the playing one
            p.playingCard = makeChiselCard(p);
            return true;


        }

        return false;
    }

    private HammerCard makeHammerCard(Player p) {
        HammerCard hc = new HammerCard();
        hc.setSledToShipAction(sledToShipAction);
        hc.player = p;
        return hc;
    }

    private LeverCard makeLeverCard(Player p) {
        LeverCard lc = new LeverCard();
        lc.setSailAction(sailAction);
        lc.setLeverAction(leverAction);
        lc.player = p;
        return lc;
    }

    private SailCard makeSailCard(Player p) {
        SailCard sc = new SailCard();
        sc.setSailAction(sailAction);
        sc.setSledToShipAction(sledToShipAction);
        sc.player = p;
        return sc;
    }

    private ChiselCard makeChiselCard(Player p) {
        ChiselCard cc = new ChiselCard();
        cc.setSledToShipAction(sledToShipAction);
        cc.player = p;
        return cc;
    }


}
