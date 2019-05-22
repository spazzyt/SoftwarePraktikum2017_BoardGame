package ch.uzh.ifi.seal.soprafs17.entity;

import ch.uzh.ifi.seal.soprafs17.gamelogic.SiteBoard;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;

@Entity
public class Move implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue
	private Long id;
	
    @ManyToOne
    @JoinColumn(name="GAME_ID")
    private Game game;


	@JsonIgnoreProperties("player")
	@ManyToOne
    @JoinColumn(name="USER_ID")
    private User user;

    @Column(name="target")
    public String to;


    public Integer ShipID;

    @Column(name = "origin")
    public String from;

	@JsonIgnore
	@Transient
	public transient SiteBoard toSite;


	public Integer pos;

	public ShipToken ship;


	@Transient
	public ArrayList<Integer> LeverStones;


	public Move() {
		// needed for db serialization
	}

	public Move(Move o) {
		this.from = o.from;
		this.to = o.to;
		this.pos = o.pos;
		this.ShipID = o.ShipID;
	}

	public Move(String from, String to, Integer pos) {
		this.from = from;
		this.to = to;
		this.pos = pos;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Game getGame() {
		return game;
	}

	public void setGame(Game game) {
		this.game = game;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}


	@Override
	public String toString() {
		return String.format("Move[from: %s to: %s pos: %d]", from, to, pos);
	}
}
