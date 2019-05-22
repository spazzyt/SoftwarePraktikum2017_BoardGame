package ch.uzh.ifi.seal.soprafs17.entity;

import ch.uzh.ifi.seal.soprafs17.gamelogic.Player;
import ch.uzh.ifi.seal.soprafs17.constant.UserStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "users")
public class User implements Serializable {

	private static final long serialVersionUID = 1L;


	@Id
	@GeneratedValue
	private Long id;
	
	@Column(nullable = false, unique = true) 
	private String username;
	
	@Column(nullable = false, unique = true) 
	private String token;
	
	@Column(nullable = false) 
	private UserStatus status;

	@JsonIgnore
    @ManyToMany
    private List<Game> games = new ArrayList<>();
	
    @OneToMany(mappedBy="user")
    private List<Move> moves;


    public User() {}
    public User(String name) {
    	this.username = name;
	}

    public transient Player player;

    //isRarePepe = Bot
    public boolean isRarePepe = false;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public List<Game> getGames() {
		return games;
	}

	public List<Move> getMoves() {
		return moves;
	}

	public void setMoves(List<Move> moves) {
		this.moves = moves;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public UserStatus getStatus() {
		return status;
	}

	public void setStatus(UserStatus status) {
		this.status = status;
	}

	public void deleteGameById(Long id){
		for(int i = 0; i < games.size(); i++){
			if(games.get(i).getId().equals(id)){
				games.remove(games.get(i));
			}
		}
	}


}
