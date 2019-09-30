package com.qq.server;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import com.qq.msg.IMsgConstance;
import com.qq.util.H2Server;
import com.qq.util.LogTools;

/**
 * ������������ ��������������  ���ҵȴ��ͻ��˵����� 
 * @author yy
 *
 */
public class Main extends Thread {
	
	private int port;	//�������˿�
	private ServerSocket ss = null;
	private JFrame frame = null		;
	/**
	 * �������������� ���Ҵ���˿ں���
	 * @param port
	 */
	public Main(int port) {
		this.port = port;
	}
	
	public void run() {
		frame = new JFrame("����������");
		frame.setLayout(null);
		frame.setBounds(0,0,400,200);
		
		JButton open = new JButton("����������");
		JButton close = new JButton("�رշ�����");
		open.setBounds(60,30,100,40);
		close.setBounds(240,30,100,40);
		
		frame.add(open);
		frame.add(close);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		
		
		open.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(ss != null) {
					JOptionPane.showMessageDialog(null, "������:"+ss.getLocalPort()+"�ѿ���,���ظ�����");
				}else {
					
					new Thread(new Runnable() {
						
						@Override
						public void run() {
							setupServer();
						}
					}).start();
				}
			}
		});
		
		
		
		close.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					ss.close();
					ss = null;
					JOptionPane.showMessageDialog(null, "�������ѹر�");
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		
	}
	
	/**
	 * ��ָ���Ķ˿�������������
	 */
	private void setupServer() {
		try {
			ss = new ServerSocket(port);
			LogTools.INFO(this.getClass(), "�����������ɹ�:" + port);
			JOptionPane.showMessageDialog(null, "������:"+ss.getLocalPort()+" �����ɹ�");
			
			System.out.println(11);
			
			File imgFile = new File("F:/QQimg"); 
			if(!imgFile.exists()) {
				imgFile.mkdirs();
				File img = new File("./images/default_header.jpg");
				FileInputStream fins = new FileInputStream(img);
				FileOutputStream fos = new FileOutputStream("F:/QQimg/default_header.jpg");
				byte[] data = new byte[1024];
				int len = 0;
				while((len = fins.read(data)) != -1) {
					fos.write(data, 0, len);
				}
				fos.close();
			}else {
				if(!new File("F:/QQimg/default_header.jpg").exists()) {
					File img = new File("./images/default_header.jpg");
					FileInputStream fins = new FileInputStream(img);
					FileOutputStream fos = new FileOutputStream("F:/QQimg/default_header.jpg");
					byte[] data = new byte[1024];
					int len = 0;
					while((len = fins.read(data)) != -1) {
						fos.write(data, 0, len);
					}
					fos.close();
					
					
				}
			}
			
			File file = new File("E:/h2/qq.mv.db");
			if(!file.exists()) {
				new H2Server().start();
			}
			
			/**
			 * ÿ����һ���ͻ����߳��������ӷ�����  ������һ���߳�ȥ������
			 */
			while(true) {
				Socket client = ss.accept();
				String cAdd = client.getRemoteSocketAddress().toString();
				LogTools.INFO(this.getClass(), "��������:" + cAdd);
				ServerThread ct = new ServerThread(client);
				ct.start();		//����һ���߳�ȥ��������ͻ���
			}
		} catch (IOException e) {
			LogTools.ERROR(this.getClass(), "����������ʧ��:" + e);
		}
	}
	
	
	public static void main(String[] args) {
		Main main = new Main(IMsgConstance.serverPort);
		main.start();
	}
}
