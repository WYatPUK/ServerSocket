package main;

import java.util.ArrayList;

public class Player {

	private long Gen_Time;
	private long Last_Time;
	public String Name;
	public String Password;
	public int Index;
	public ArrayList<Integer> Knowledge;
	public ArrayList<MessageEvent> MessageList;
	
	public Player(String Name_, String Password_, int Index_) {
		Name = Name_;
		Password = Password_;
		Index = Index_;
		Knowledge = new ArrayList<Integer>();
		MessageList = new ArrayList<MessageEvent>();
	}

	@Override
	public boolean equals(Object other){
		if (other instanceof Player) {
			Player oplayer = (Player)other;
			return (Name.equals(oplayer.Name));
		}
		return super.equals(other);
	}
	
	public boolean Is_Match(String password) {
		return password.equals(Password);
	}
	
	public Player Set_Time(long Time_) {
		Gen_Time = Time_;
		Last_Time = Time_;
		return this;
	}
	
	public long get_birth_time(long Time_) {
		return Time_ - Gen_Time;
	}
	
	public long get_active_time(long Time_) {
		return Time_ - Last_Time;
	}
	
	public void Add_MessageEvent(MessageEvent msgE) {
		MessageList.add(new MessageEvent(msgE));
	}
	
	public MessageEvent Get_A_MessageEvent() {
		if (MessageList.isEmpty()) {
			return null;
		}
		MessageEvent msgE = new MessageEvent(MessageList.get(0));
		MessageList.remove(0);
		return msgE;
	}
	
}
