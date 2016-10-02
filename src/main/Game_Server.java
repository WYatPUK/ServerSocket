package main;

import java.util.ArrayList;

public class Game_Server {
	
	ArrayList<Player> playerList;
	
	public Game_Server() {
		
	}
	public boolean Add_Member (String A) {
		if (playerList.contains(new Player(A))) return false;
		playerList.add(new Player(A).Set_Time(System.currentTimeMillis()));
		return true;
	}
	
	public String handle_Message(String msg) {
		return "NULL";
	}
	
}
