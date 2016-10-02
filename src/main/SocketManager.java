package main;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketManager {

	public static ServerSocket ss;
	private static boolean open;
	private static int port;
	
	public SocketManager(int port_) throws IOException {
		port = port_;
		open = false;
		ss = new ServerSocket(port);
		open = true;
	}
	
	public Socket accept() throws IOException {
		return ss.accept();
	}
	
	protected void finalize() throws IOException {
		if (open) {
			ss.close();
			open = false;
		}
	}
	
	public void Open() throws IOException {
		if (! open) {
			ss = new ServerSocket(port);
			open = true;
		}
	}
	
	public void Close() throws IOException {
		if (open) {
			ss.close();
			open = false;
		}
	}
	
	public boolean Is_Open () {
		return open;
	}
	
}
