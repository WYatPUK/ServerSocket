package main;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

public class Window implements ActionListener{

	private final static int Port = 30002;
	
	private static String Show_Info;
	private static SocketManager mSocketManager;
	private static Game_Server gameServer;
	private static boolean Change_Socket_State_Flag;
	
	private static Socket s;
	
	static JButton btnSocket = new JButton("Open/Close Socket Server");
	static JLabel lblSocket = new JLabel("Socket has been closed");
	static JTextArea textArea = new JTextArea();
	//static JScrollBar scrollBar = new JScrollBar();
	static JScrollPane scrollPane = new JScrollPane();
	private JFrame frame;

	/**
	 * Launch the application.
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws IOException, InterruptedException {
		Show_Info = "";
		Change_Socket_State_Flag = false;
		gameServer = new Game_Server();
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Window window = new Window();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
		byte[] buffer = new byte[1024];
		
		Println("Waiting to open ServerSocket");
		mSocketManager = new SocketManager(Port); //Initiate a SocketManager
		btnSocket.setEnabled(true); //release the button
		lblSocket.setText("Socket has been opened");
		Println("Open ServerSocket succeed");
		while (true) {
			if (Change_Socket_State_Flag) {
				Change_Socket_State();
				Change_Socket_State_Flag = false;
			}
			if (!mSocketManager.Is_Open()) {
				Thread.sleep(10);
				continue;
			}
			s = mSocketManager.accept(); //stay here
			long first_Time = System.currentTimeMillis();
			Println("Success to accept a socket");
			OutputStream os = s.getOutputStream();
			InputStream is = s.getInputStream();
			int Len = is.read(buffer);
			if (Len != -1) {
				String msg = new String(buffer, 0, Len, "UTF-8");
				Println("Success to input message: " + msg);
				String re = gameServer.handle_Message(msg);
				Println("Output message: " + re);
				Println(gameServer.Info());
				os.write((re/* + ' ' + "At:" + System.currentTimeMillis()*/).getBytes("utf-8"));
			}
			else {
				Println("Failed to input message.");
				os.write("Failed".getBytes("utf-8"));
			}
			is.close();
			os.close(); 
			s.close();
			long last_Time = System.currentTimeMillis();
			Println("Time(ms):" + (last_Time-first_Time));
		}
		
	}
	
	public Window() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame("Socket Console");
		frame.setResizable(false);
		frame.setBounds(100, 100, 1080, 768);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		btnSocket.setFont(new Font("Copperplate Gothic Bold", Font.BOLD, 18));
		frame.getContentPane().add(btnSocket, BorderLayout.SOUTH);
		lblSocket.setFont(new Font("Agency FB", Font.PLAIN, 35));
		lblSocket.setHorizontalAlignment(SwingConstants.CENTER);
		frame.getContentPane().add(lblSocket, BorderLayout.NORTH);
		frame.getContentPane().add(scrollPane, BorderLayout.CENTER);
		scrollPane.setViewportView(textArea);
		//frame.getContentPane().add(scrollBar, BorderLayout.EAST);
		textArea.setEditable(false);
		btnSocket.addActionListener(this);
		//scrollBar.addAdjustmentListener(this);
		textArea.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 20));
		btnSocket.setEnabled(false);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getActionCommand()) {
		case "Open/Close Socket Server":
			Change_Socket_State_Flag = true;
			break;
		}
	}
	
	public static void Println(String A) {
		if (!Show_Info.isEmpty()) Show_Info += '\n';
		Show_Info += A;
		textArea.setText(Show_Info);
	}
	
	public static void Change_Socket_State() {
		if (mSocketManager.Is_Open()) {
			try {
				mSocketManager.Close();
				lblSocket.setText("Socket has been closed");
				Println("Close ServerSocket succeed");
			} catch (IOException e) {
				Println("It's very strange! Close ServerSocket failed! Please inform the writer!");
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			try {
				mSocketManager.Open();
				lblSocket.setText("Socket has been opened");
				Println("Open ServerSocket succeed");
			} catch (IOException e) {
				Println("It's very strange! Open ServerSocket failed! Please inform the writer!");
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
