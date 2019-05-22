package ch.uzh.ifi.seal.soprafs17.gamelogic;

import ch.uzh.ifi.seal.soprafs17.gamelogic.marketcards.MarketCard;
import ch.uzh.ifi.seal.soprafs17.gamelogic.marketcards.bluecards.MultiMoveCard;
import ch.uzh.ifi.seal.soprafs17.constant.PlayerFieldColour;

import javax.persistence.Embeddable;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Embeddable
public class Player {

    //public int id;

    @Transient
    public PlayerFieldColour colour;
    @Transient
    public List<MarketCard> marketCards = new ArrayList<>();
    public Decision decision;
    public Quarry quarry;
    public Sled sled;

    public Integer score = 0;

    @Transient
    public PlayerCardStack cardStack;

    @Transient
    public MultiMoveCard playingCard;

    public Player() {}

    public Player(PlayerFieldColour colour, int sledSize){
        this.colour = colour;
        sled = new Sled(colour, sledSize); //should know how many player there are
        //sled.quarry sollte gar nicht existieren
        quarry = sled.quarry;

        cardStack = new PlayerCardStack(this);
    }

}
