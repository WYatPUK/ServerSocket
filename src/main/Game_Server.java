package main;

import java.util.ArrayList;

public class Game_Server {
	
	/*
	 * we can force everyone to exit by shut down the Server for at least 5 seconds
	 */
	
	ArrayList<Player> playerList;
	private int Radio_Pixel = 50;
	private int Member = 10; // max member is 40;
	
	public Game_Server() {
		playerList = new ArrayList<Player>();
	}
	public boolean Add_Member (String name, String password) {
		if (playerList.contains(new Player(name, password, 0))) {
			int Index = playerList.indexOf(new Player(name, password, 0));
			if (playerList.get(Index).Is_Match(password)) {
				return true;
			} else {
				return false;
			}
		}
		Player A = new Player(name, password, playerList.size());
		A.Set_Time(System.currentTimeMillis());
		playerList.add(A);
		return true;
	}
	
	private String Register_Cmd = "Register:";
	private String Radio_Cmd = "Ask_Radio?";
	private String Shout_Cmd = "Shout:";

	public String handle_Message(String msg) {
		String re = "NULL";
		if (msg.startsWith(Register_Cmd)) {
			String sbs1 = msg.substring(Register_Cmd.length());
			if (sbs1.indexOf('#') != sbs1.length()) {
				String name = sbs1.substring(0, sbs1.indexOf('#'));
				String password = sbs1.substring(sbs1.indexOf('#')+1);
				if (Add_Member(name, password)) {
					if (playerList.indexOf(new Player(name, password, 0)) == Member) {
						//cannot add more people
						playerList.remove(Member);
						re = "Member list has been full";
					} else {
						re = "SuccessR/L:" + playerList.indexOf(new Player(name, password, 0));// + name + "#" + password;
					}
				}
				else {
					re = "User Already Existed or Password Wrong";
				}
			}
		}
		else if (msg.equals(Radio_Cmd)) {
			re = "Radio_Pixel=" + Radio_Pixel;
			re += '&';
			re += "Member=" + Member;
		}
		else if (msg.startsWith(Shout_Cmd)) {
			String sbs1 = msg.substring(Shout_Cmd.length());
			String spt[] = sbs1.split("&");
			String sbs2;
			String Say = "";
			int Knowledge = -1;
			int From_Index = -1;
			int Target_Index = -1;
			for (int i=0; i<spt.length; i++) {
				if (spt[i].startsWith("At:")) {
					sbs2 = spt[i].substring("At:".length());
					Target_Index = Integer.parseInt(sbs2);
				} else if (spt[i].startsWith("From:")) {
					sbs2 = spt[i].substring("From:".length());
					From_Index = Integer.parseInt(sbs2);
				} else if (spt[i].startsWith("Knowledge:")) {
					sbs2 = spt[i].substring("Knowledge:".length());
					Knowledge = Integer.parseInt(sbs2);
				} else if (spt[i].startsWith("Say:")) {
					Say = spt[i].substring("Say:".length());
				}
			}
			if (Knowledge > 0 && From_Index > 0 && Target_Index > 0) { //necessary
				if (From_Index < playerList.size()) {
					if (Target_Index < playerList.size()) {
						re = "Send Succeed";
						playerList.get(Target_Index).Add_MessageEvent(new MessageEvent(Say, Knowledge, From_Index));
					} else {
						//not send yet
						re = "Player Not Exist";
					}
				} else {
					re = "Need to relogin";
				}
			} else {
				re = "Lost Infomation";
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
