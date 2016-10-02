package main;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {

	public static void main (String arg[]) throws IOException {
		System.out.println("My first Java project");
		ServerSocket ss = new ServerSocket(30002);
		Socket s = ss.accept();
		OutputStream os = s.getOutputStream();
		os.write("Hello, I'm Wy from the server".getBytes("utf-8"));
		os.write("mama send a message to me".getBytes("utf-8"));
		System.out.println("Finished to send a message!");
		os.close();
		s.close();
		ss.close();
	}
	
}
