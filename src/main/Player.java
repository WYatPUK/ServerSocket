package main;

import java.util.ArrayList;

public class Player {

	private long Gen_Time;
	private long Last_Time;
	public String Name;
	public String Password;
	public int Index;
	public int Basic_Improve_ms;
	public ArrayList<MessageEvent> MessageList;
	public ArrayList<Boolean> Knowledge_Got;
	
	public Player(String Name_, String Password_, int Index_, int Basic_Improve_ms_) {
		Name = Name_;
		Password = Password_;
		Index = Index_;
		MessageList = new ArrayList<MessageEvent>();
		Knowledge_Got = new ArrayList<Boolean>();
		for (int i=0; i<Game_Server.Knowledge_Amount; i++) {
			Knowledge_Got.add(false);
		}
		Basic_Improve_ms = Basic_Improve_ms_;
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
	
	public boolean If_New_Knowledge_And_Generate() {
		if (System.currentTimeMillis() - this.Last_Time > Basic_Improve_ms) {
			this.Last_Time = System.currentTimeMillis();
			MessageList.add(new MessageEvent("", 666, 0));
			return true;
		}
		return false;
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
	
	public boolean Save_Knowledge_Got (int num) {
		if (num < Game_Server.Knowledge_Amount) {
			this.Knowledge_Got.set(num, true);
			return true;
		}
		return false;
	}
	
	public String Get_Saved() {
		String A = "";
		for (int i=0; i<Game_Server.Knowledge_Amount; i++) {
			if (this.Knowledge_Got.get(i)) {
				A += "&Kn:" + i;
			}
		}
		return A;
	}
	
}
