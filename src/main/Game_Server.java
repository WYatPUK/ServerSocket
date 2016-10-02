package main;

import java.util.ArrayList;

public class Game_Server {
	
	ArrayList<Player> playerList;
	
	public Game_Server() {
		playerList = new ArrayList<Player>();
	}
	public boolean Add_Member (String name, String password) {
		if (playerList.contains(new Player(name, password))) {
			int Index = playerList.indexOf(new Player(name, password));
			if (playerList.get(Index).Is_Match(password)) {
				return true;
			} else {
				return false;
			}
		}
		Player A = new Player(name, password);
		A.Set_Time(System.currentTimeMillis());
		playerList.add(A);
		return true;
	}
	
	private String Register_Cmd = "Register:";
	
	public String handle_Message(String msg) {
		String re = "NULL";
		if (msg.startsWith(Register_Cmd)) {
			String sbs1 = msg.substring(Register_Cmd.length());
			if (sbs1.indexOf('#') != sbs1.length()) {
				String name = sbs1.substring(0, sbs1.indexOf('#'));
				String password = sbs1.substring(sbs1.indexOf('#')+1);
				if (Add_Member(name, password)) {
					re = "Success:" + name + "#" + password;
				}
				else {
					re = "User Already Existed or Password Wrong";
				}
			}
		}
		return re;
	}
	
	public String Info() {
		String info = "";
		info += "Size" + playerList.size() + '\n';
		for (int i=0; i<playerList.size(); i++) {
			info += "Player:" + i + " Name:" + playerList.get(i).Name + " Password:" + playerList.get(i).Password + '\n';
			
		}
		return info;
	}
	
}
