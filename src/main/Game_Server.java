package main;

import java.util.ArrayList;

import javax.swing.JOptionPane;

public class Game_Server {
	
	/*
	 * we can force everyone to exit by shut down the Server for at least 5 seconds
	 * (already tested)
	 * 
	 * keep player 0 as administrator
	 * already in the group as the Server is opened
	 * 
	 * knowledge 666 means to get a lowest new knowledge card
	 */
	
	ArrayList<Player> playerList;
	private int Radio_Pixel = 10;
	private int Member = 10; // max member is 40;
	private int Basic_Improve_ms = 20000; // every 20s get a new knowledge card; should not be too small
	static public int Knowledge_Amount = 5; // 5 knowledge yet
	
	public Game_Server() {
		playerList = new ArrayList<Player>();
		playerList.add(new Player("Admin", "pkuMakerspace2016", 0, Basic_Improve_ms));
	}
	public boolean Add_Member (String name, String password) {
		if (playerList.contains(new Player(name, password, 0, Basic_Improve_ms))) {
			int Index = playerList.indexOf(new Player(name, password, 0, Basic_Improve_ms));
			if (playerList.get(Index).Is_Match(password)) {
				return true;
			} else {
				return false;
			}
		}
		Player A = new Player(name, password, playerList.size(), Basic_Improve_ms);
		A.Set_Time(System.currentTimeMillis());
		playerList.add(A);
		return true;
	}
	
	private String Register_Cmd = "Register:";
	private String Radio_Cmd = "Ask_Radio?From:";
	private String Shout_Cmd = "Shout:";

	public String handle_Message(String msg) {
		String re = "NULL";
		if (msg.startsWith(Register_Cmd)) {
			String sbs1 = msg.substring(Register_Cmd.length());
			if (sbs1.indexOf('#') != sbs1.length()) {
				String name = sbs1.substring(0, sbs1.indexOf('#'));
				String password = sbs1.substring(sbs1.indexOf('#')+1);
				if (Add_Member(name, password)) {
					if (playerList.indexOf(new Player(name, password, 0, Basic_Improve_ms)) == Member) {
						//cannot add more people
						playerList.remove(Member);
						re = "Member list has been full";
						JOptionPane.showMessageDialog(null, "Member list has been full", "Warning",JOptionPane.WARNING_MESSAGE);  
					} else {
						re = "SuccessR/L:" + playerList.indexOf(new Player(name, password, 0, Basic_Improve_ms));// + name + "#" + password;
					}
				}
				else {
					re = "User Already Existed or Password Wrong";
				}
			}
		}
		else if (msg.startsWith(Radio_Cmd)) {
			int Index = Integer.parseInt(msg.substring(Radio_Cmd.length()));
			re = "Radio_Pixel=" + Radio_Pixel;
			re += '&';
			re += "Member=" + Member;
			re += playerList.get(Index).Get_Saved();
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
			if (Knowledge > -1 && From_Index > -1 && Target_Index > -1) { //necessary
				if (From_Index < playerList.size()) {
					if (Target_Index < playerList.size()) {
						re = "Send Succeed";
						playerList.get(Target_Index).Add_MessageEvent(new MessageEvent(Say, Knowledge, From_Index));
					} else {
						//not send yet
						re = "Player Not Exist";
					}
				} else {
					re = "Need to relogin"; //should not reach this
					this.Toast("Need to relogin: " + From_Index);
				}
			} else {
				re = "Lost Infomation";
				this.Toast("Knowledge=" + Knowledge + '\n' + "From_Index=" + From_Index + '\n' +
						"Target_Index=" + Target_Index);
			}
		}
		else if (msg.startsWith("Refresh:From:")) {
			String sbs1 = msg.substring("Refresh:From:".length());
			int Index = Integer.parseInt(sbs1);
			if (Index < playerList.size()) {
				playerList.get(Index).If_New_Knowledge_And_Generate();
				MessageEvent msgE = playerList.get(Index).Get_A_MessageEvent();
				if (msgE == null) {
					re = "Nothing to Refresh";
				} else {
					re = "Receive:" + msgE.Wy_ToString();
				}
			} else {
				re = "Need to relogin"; //should not reach this
				JOptionPane.showMessageDialog(null, "Need to relogin: " + Index, "Warning",JOptionPane.WARNING_MESSAGE);
			}
		}
		else if (msg.startsWith("Save:From:")) {
			String sbs1 = msg.substring("Save:From:".length());
			String spt[] = sbs1.split("&");
			int From_Index = -1;
			if (spt.length > 0) {
				From_Index = Integer.parseInt(spt[0]);
				if (From_Index != -1) {
					for (int i=1; i<spt.length; i++) {
						this.playerList.get(From_Index).Save_Knowledge_Got(Integer.parseInt(spt[i]));
					}
					re = "Save Finished";
				} else {
					re = "From -1";
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
	
	private void Toast(String str) {
		JOptionPane.showMessageDialog(null, str, "Warning",JOptionPane.INFORMATION_MESSAGE); 
	}
	
}
