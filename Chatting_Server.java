

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class Chatting_Server {

	public static void main(String[] args) {
		MyFrame mf = new MyFrame();
		new Thread(mf,"receive").start();
		

	}

}

class MyFrame extends JFrame implements ActionListener,Runnable {
	private JPanel jp;
	private JTextArea jta;
	private JTextArea jta2;
	private JButton jb;
	private ServerSocket ss = null;
	private Socket s = null;
	private DataInputStream dis = null;
	private DataOutputStream dos = null;

	public MyFrame() {
		this.setBounds(400, 100, 400, 450);
		this.setDefaultCloseOperation(3);
		this.setTitle("服务端");
		jp = new JPanel();
		jp.setBackground(Color.GREEN);
		jta = new JTextArea(15, 35);
		jta2 = new JTextArea(5, 35);
		jb = new JButton("发送");
		jp.add(jta);
		jp.add(jta2);
		jp.add(jb);
		this.add(jp);
		jb.addActionListener(this);
		this.setVisible(true);
		try {
			ss = new ServerSocket(6666);
			System.out.println("服务器开启，等待连接。。。");
			s = ss.accept();
			System.out.println("有客户端连接");
			dis = new DataInputStream(s.getInputStream());
			dos = new DataOutputStream(s.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	
	public void actionPerformed(ActionEvent e) {
		send();
	}


	
	public void run() {
		recevie();
	}
	
	public void recevie() {
		while(true) {
			InetAddress IP = s.getInetAddress();
			Date time = new Date();
			try {
				String msg = "客户端    "+IP + " " + time.toLocaleString() + "\n" + dis.readUTF()+"\n";
				jta.setText(jta.getText()+msg);
			} catch (IOException e) {
				System.err.println("客户端关闭");
				break;
			}
		}
	}
	public void send() {
		String sendmsg = jta2.getText();
		if(sendmsg.equals("")) {
			JOptionPane.showMessageDialog(this, "请输入要发送的内容");
		}else {
			try {
				Date time = new Date();
				dos = new DataOutputStream(s.getOutputStream());
				dos.writeUTF(sendmsg);
				dos.flush();
				jta.setText(jta.getText()+"服务器  "+s.getLocalAddress() +" "+time.toLocaleString()+"\n" +sendmsg+"\n");
				jta2.setText("");
			} catch (IOException e) {
				System.err.println("客户端关闭");
			}
		}
	}
}