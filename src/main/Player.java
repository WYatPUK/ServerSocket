package main;

public class Player {

	private long Gen_Time;
	private long Last_Time;
	private String Name;
	
	public Player(String Name_) {
		Name = Name_;
	}

	public boolean equals(Object other){
		Player oplayer = (Player)other;
		return (Name == oplayer.Name);
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
	
}
