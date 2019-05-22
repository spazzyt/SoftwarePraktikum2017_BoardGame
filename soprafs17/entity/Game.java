package ch.uzh.ifi.seal.soprafs17.entity;

import ch.uzh.ifi.seal.soprafs17.gamelogic.*;
import ch.uzh.ifi.seal.soprafs17.constant.GameStatus;
import ch.uzh.ifi.seal.soprafs17.constant.PlayerFieldColour;
import ch.uzh.ifi.seal.soprafs17.web.websockets.messages.WSMessage;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import javax.persistence.*;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
public class Game implements Serializable {

	@JsonIgnore
	@Transient
	private final transient Logger log = LoggerFactory.getLogger(Game.class);

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue
	private Long id;
	
	@Column
	private String name; //the game wont have a name in the front end

	@Column(nullable = false) 
	private String owner;
	
	@Column 
	private GameStatus status;
	
	@Column 
	private Integer currentPlayer;

	@Column
	private Integer numPlayers;

    @OneToMany(mappedBy="game")
    private List<Move> moves;
    
    @ManyToMany(mappedBy="games")
	@JsonIgnoreProperties("games")
    public List<User> players;


	@JsonIgnore
	@Transient
    public transient TurnManager turnManager;

	@JsonIgnore
	@Transient
    public transient Map<String,SiteBoard> siteBoards;

	@JsonIgnore
	@Transient
	public transient Map<String, WebSocketSession> wsSessions;


	@JsonIgnore
	@Transient
	public transient List<TextMessage> msgLog = new ArrayList<>();


    public Game() {

    	players = new ArrayList<>();
    	siteBoards = new HashMap<>();


    	wsSessions = new HashMap<>();

	}

	//TODO: keep up to date with new sites
	public boolean setUpGame() {
		Obelisk o = new Obelisk(numPlayers);
		Temple t = new Temple(numPlayers);
		BurialChamber b = new BurialChamber();
		Pyramid p = new Pyramid();
		siteBoards.put("Obelisk", o);
		siteBoards.put("DepartingHarbour", new DepartingHarbour());
		siteBoards.put("Temple", t);
		siteBoards.put("BurialChamber", b);
		siteBoards.put("Pyramid", p);

		Market m = new Market();
		siteBoards.put("Market", m);


		//siteBoards.put("Sled", new Sled()); //todo for every colour available
		//siteBoards.put("Quarry", new Quarry());



		turnManager = new TurnManager(siteBoards,this);


		// Initialize market cards
		m.initDemCardsYo(p,o,b,t);

		return true;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public List<Move> getMoves() {
		return moves;
	}

	public void setMoves(List<Move> moves) {
		this.moves = moves;
	}

	public List<User> getPlayers() {
		return players;
	}

	public void setPlayers(List<User> players) {
		this.players = players;
	}

	public void addPlayer(User user) {
    	players.add(user);
    	user.getGames().add(this);
    }

	public boolean removePlayer(User user) {
		return players.remove(user);
	}

	public GameStatus getStatus() {
		return status;
	}

	public void setStatus(GameStatus status) {
		this.status = status;
	}

	public Integer getCurrentPlayer() {
		return currentPlayer;
	}

	public void setCurrentPlayer(Integer currentPlayer) {
		this.currentPlayer = currentPlayer;
	}

	public Integer getNumPlayers() {
		return numPlayers;
	}

	public void setNumPlayers(Integer numPlayers) {
		this.numPlayers = numPlayers;
	}

	public User getUser(String token) {
		for(User u : players) {
			if(u.getToken().equals(token))
				return u;
		}
		return null;
	}

	public Player getPlayer(String token) {
    	for(User u : players) {
    		if(u.getToken().equals(token))
    			return u.player;
		}

		return null;
	}
	public void start() {

		// set player color
		// TODO: shuffle
		for(int i=0;i<numPlayers;i++) {
			players.get(i).player = new Player(PlayerFieldColour.values()[i+1], 2+i);
		}


			turnManager.startRound();


	}


	public void addWSSession(String uname, WebSocketSession sess) throws JsonProcessingException {
		wsSessions.put(uname, sess);
		broadcast(new WSMessage(WSMessage.MessageType.USERCONNECTED, uname));
	}

	public void broadcast(WSMessage msg) {
		ObjectMapper mapper = new ObjectMapper();


		TextMessage tm = null;
		try {
			tm = new TextMessage(mapper.writeValueAsString(msg));
		} catch (JsonProcessingException e) {
			log.error("Exception:",e);
		}

		TextMessage finalTm = tm;
		wsSessions.forEach((u, sess) -> {

			try {
				sess.sendMessage(finalTm);
			} catch (IOException e) {
				log.error("Exception:",e);
			}

			msgLog.add(finalTm);

		});
	}

	public void gameOver() {

		wsSessions.forEach((u, sess) -> {

			try {
				sess.close();
			} catch (IOException e) {
				log.error("Exception:",e);
			}
		});
	}
}
