package main;

public class MessageEvent {

	String Say = "";
	int Knowledge = -1;
	int From_Index = -1;
	
	MessageEvent(String Say_, int Knowledge_, int From_Index_) {
		Say = Say_;
		Knowledge = Knowledge_;
		From_Index = From_Index_;
	}

	public MessageEvent(MessageEvent messageEvent) {
		// TODO Auto-generated constructor stub
		Say = messageEvent.Say;
		Knowledge = messageEvent.Knowledge;
		From_Index = messageEvent.From_Index;
	}
	
}
