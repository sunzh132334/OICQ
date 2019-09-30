package com.qq.client;

import java.awt.Color;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.qq.dao.JkuserDaoImpl;
import com.qq.model.Jkuser;
import com.qq.msg.IMsgConstance;
import com.qq.msg.MsgHead;
import com.qq.util.ImageUtil;
import com.qq.util.MD5Util;

/**
 * QQ��Ŀ�������� 1.��ʾ��½������ ��½�ɹ���ת��QQ������ 2.�ṩע�Ṧ�� ע��ɹ���ת����½���� 3.�������� �Զ���¼ ��ס����
 * ѡ���½״̬�ȹ���
 * 
 * @author yy
 * 
 */
public class Main {

	private JFrame jf_login; // ��½����
	// ��½�����jknum����� ���������
	private JFormattedTextField jt_jknum;
	private JTextField jt_passwd;
	ClientConnection conn = ClientConnection.getIns();
	JTextField username; // �û��������
	JPasswordField pwd; // ���������
	JComboBox com1; // ��½״̬ѡ���
	JCheckBox c1; // ��ס����
	JCheckBox c2; // �Զ���¼
	private Jkuser user = null;
	private String[] str = new String[2];
	private int jknum = 0;
	
	/**
	 * 
	 * ��ʾ��½����
	 */
	public void showLoginUI() {
		jf_login = new JFrame("��QQͨѶ��Ŀ");
		// �����õ���һЩ���������
		JLabel jl1; // ��½ͷ��
		JLabel jl2; // ע����ʾ
		JLabel jl3; // ��������

		JButton b1; // ��¼��ť

		JLabel jl_bg = new JLabel();
		// ���ò��ַ�ʽΪ���Բ���
		jf_login.setLayout(null);
		jf_login.setBounds(0, 0, 380, 220);

		// ���ñ���ͼƬ
		ImageIcon bg_icon = new ImageIcon("./images/bg.jpg");
		jl_bg = new JLabel(bg_icon);
		jl_bg.setBounds(0, 0, bg_icon.getIconWidth(), bg_icon.getIconHeight());
		// �����ݴ���ת��ΪJPanel���������÷���setOpaque()��ʹ���ݴ���͸��
		JPanel imgPanel = (JPanel) jf_login.getContentPane();
		imgPanel.setOpaque(false);
		jf_login.getLayeredPane().setLayout(null);
		jf_login.getLayeredPane().add(jl_bg, new Integer(Integer.MIN_VALUE));
		// ����imagePane�Ĳ��ַ�ʽΪ���Բ���
		imgPanel.setLayout(null);

		// ���ñ���ͷ��
		ImageIcon icon = new ImageIcon("./images/test.jpg");
		Image logo = icon.getImage();
		jf_login.setIconImage(logo);
		com1 = new JComboBox(); // ��½״̬��ѡ��
		com1.addItem("����");
		com1.addItem("����");
		com1.setBounds(38, 105, 60, 25);
		username = new JTextField(); // QQ����
		username.setBounds(112, 35, 160, 27);
		pwd = new JPasswordField(); // QQ����
		pwd.setBounds(112, 68, 160, 27);
		c1 = new JCheckBox("��ס����"); // ��ס����ѡ��
		c1.setBounds(112, 105, 78, 25);
		c1.setContentAreaFilled(false);
		c2 = new JCheckBox("�Զ���¼"); // �Զ���¼ѡ��
		c2.setBounds(192, 105, 78, 25);
		c2.setContentAreaFilled(false);
		jl2 = new JLabel("ע���˺�"); // ע���˺�ѡ��
		jl2.setBounds(287, 35, 70, 27);
		jl2.setForeground(Color.blue);
		b1 = new JButton("��½"); // ��½��ť������
		ImageIcon login_icon = new ImageIcon("./images/login.jpg");
		b1.setIcon(login_icon);
		b1.setBounds(143, 140, login_icon.getIconWidth() - 10,
				login_icon.getIconHeight());
		jl3 = new JLabel("��������");
		jl3.setBounds(287, 70, 70, 27);
		jl3.setForeground(Color.blue);
		// imagePane������ĳ�ʼ��
		jl1 = new JLabel(); // ��½ͷ��
		ImageIcon login_img = null;
		File saveFile = new File("F:/QQmsg/user.dat");
		Jkuser jkuser = null;
		if (saveFile.exists()) {
			ObjectInputStream oins = null;
			try {
				oins = new ObjectInputStream(new FileInputStream(saveFile));
				jkuser = (Jkuser) oins.readObject();
				if(jkuser.getIconpath() != null) {
					login_img = new ImageIcon(ImageUtil.getBytesFromFile(jkuser
							.getIconpath()));
				} else {
					login_img = new ImageIcon("./images/logo.jpg");
				}
				username.setText(jkuser.getJknum() + "");
			} catch (Exception e1) {
				e1.printStackTrace();
			} finally {
				try {
					oins.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		} else {
			login_img = new ImageIcon("./images/logo.jpg");
		}

		jl1.setIcon(login_img);
		jl1.setBounds(38, 40, login_img.getIconWidth(),
				login_img.getIconHeight());

		// ��imagePane��������
		imgPanel.add(jl1);
		imgPanel.add(com1);
		imgPanel.add(username);
		imgPanel.add(pwd);
		imgPanel.add(c1);
		imgPanel.add(c2);
		imgPanel.add(jl2);
		imgPanel.add(b1);
		imgPanel.add(jl3);

		// ���ּ����¼��Ĵ���

		// ע���˺��¼�����
		jl2.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				showRegUI();
			}

		});
		// ���������¼�����
		jl3.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				showFindPwdUI();
			}
		});

		// �Զ���¼���� ���ѡ�����Զ���¼ ����û��ѡ�б������� ��ô�ѱ�������ѡ��
		c2.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (c2.isSelected()) {
					if (!c1.isSelected()) {
						c1.setSelected(true);
					}
				}
			}
		});

		// ��¼��ť���¼�����
		b1.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					loginAction();
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				}
			}
		});
		b1.setMnemonic(KeyEvent.VK_ENTER);

		jf_login.setResizable(false);
		jf_login.setLocationRelativeTo(null);
		jf_login.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		// ����Ƿ���Ҫ������� �Ƿ���Ҫ�Զ���¼
		File check = new File("F:/QQmsg/state.dat");
		if (check.exists()) {
			BufferedReader br = null;
			boolean flag = false;
			try {
				br = new BufferedReader(new FileReader(check));
				String n1 = br.readLine().trim();
				String n2 = br.readLine().trim();
				if (n1.equals("1")) {
					pwd.setText(jkuser.getPassword());
					c1.setSelected(true);
					if (n2.equals("1")) {
						c2.setSelected(true);
						// �ж��Ƿ��Ѿ���½
						File loginstate = new File("F:/QQmsg/loginstate.dat");
						if (loginstate.exists()) {
							ArrayList<Integer> list;
							try {
								ObjectInputStream oins = new ObjectInputStream(
										new FileInputStream(loginstate));
								list = (ArrayList<Integer>) oins.readObject();
								if (list.indexOf((Integer) jkuser.getJknum()) != -1) {
									flag = true;
								}
							} catch (IOException e) {
								e.printStackTrace();
							} catch (ClassNotFoundException e) {
								e.printStackTrace();
							}
						}
						if (!flag)
						{
							this.loginAction();
							return;
						}
					}
				}
			} catch (Exception e1) {
				e1.printStackTrace();
			} finally {
				try {
					br.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
		
		jf_login.setVisible(true);

	}

	/**
	 * �һ��������
	 */
	private void showFindPwdUI() {
		final JFrame find = new JFrame("�һ�����");
		JLabel label = new JLabel("����������QQ����:");
		JButton btn = new JButton("��һ��");
		final JTextField f1 = new JTextField();
		// ���ñ���ͼƬ
		ImageIcon bg_icon = new ImageIcon("./images/reg_bg.jpg");
		JLabel l1 = new JLabel(bg_icon);
		l1.setBounds(0,0,300,140);
		find.setBounds(0, 0, 300,140);
		// �����ݴ���ת��ΪJPanel���������÷���setOpaque()��ʹ���ݴ���͸��
		JPanel imgPanel = (JPanel) find.getContentPane();
		imgPanel.setOpaque(false);
		find.getLayeredPane().setLayout(null);
		find.getLayeredPane().add(l1, new Integer(Integer.MIN_VALUE));
		// ����imagePane�Ĳ��ַ�ʽΪ���Բ���
		imgPanel.setLayout(null);
		// ���ñ���ͷ��
		ImageIcon icon = new ImageIcon("./images/test.jpg");
		Image logo = icon.getImage();
		find.setIconImage(logo);
		
		
		label.setBounds(20,30,140,25);
		f1.setBounds(165,30,90,25);
		btn.setBounds(85,75,100,35);
		
		//������
		find.add(label);
		find.add(f1);
		find.add(btn);
		
		//��Ӽ����¼�
		btn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String num = f1.getText().trim();
				if(num.equals("") || num == null) {
					JOptionPane.showMessageDialog(find, "QQ���벻��Ϊ��!");
					return;
				}
				
				try {
					jknum = Integer.parseInt(num);
				} catch (NumberFormatException e1) {
					JOptionPane.showMessageDialog(find, "QQ�������������!");
					return;
				}
				

				if(conn.conn2Server()) {
					str = conn.forgetPwd(jknum);
					if(str == null) {
						JOptionPane.showMessageDialog(find, "QQ���벻����!");
						return;
					}else {
						find.dispose();showFindUI2();
					}
				}
				
				
			}
		});
		
		find.setResizable(false);
		find.setVisible(true);
		find.setLocationRelativeTo(null);
		find.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}

	/**
	 * �һ�����ڶ���UI
	 * @param user
	 */
	protected void showFindUI2() {
		final JFrame find = new JFrame("�һ�����");
		JLabel label = new JLabel("���뱣������:");
		JButton btn = new JButton("��һ��");
		JLabel label2 = new JLabel(str[0].trim());
		JLabel label3 = new JLabel("����д��ȷ��");
		final JTextField f1 = new JTextField();
		// ���ñ���ͼƬ
		ImageIcon bg_icon = new ImageIcon("./images/reg_bg.jpg");
		JLabel l1 = new JLabel(bg_icon);
		l1.setBounds(0,0,300,200);
		find.setBounds(0, 0, 300,200);
		// �����ݴ���ת��ΪJPanel���������÷���setOpaque()��ʹ���ݴ���͸��
		JPanel imgPanel = (JPanel) find.getContentPane();
		imgPanel.setOpaque(false);
		find.getLayeredPane().setLayout(null);
		find.getLayeredPane().add(l1, new Integer(Integer.MIN_VALUE));
		// ����imagePane�Ĳ��ַ�ʽΪ���Բ���
		imgPanel.setLayout(null);
		// ���ñ���ͷ��
		ImageIcon icon = new ImageIcon("./images/test.jpg");
		Image logo = icon.getImage();
		find.setIconImage(logo);
		
		
		label.setBounds(20,30,140,25);
		label2.setBounds(130,30,90,25);
		btn.setBounds(85,120,100,35);
		label3.setBounds(20,65,140,25);
		f1.setBounds(130,65,90,25);
		
		
		//������
		find.add(label);
		find.add(label2);
		find.add(btn);
		find.add(label3);
		find.add(f1);
		
		
		//��Ӽ����¼�
		btn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String str = f1.getText().trim();
				if(str == null || str.equals("")) {
					JOptionPane.showMessageDialog(find, "�𰸲���Ϊ��!");
					return;
				}else if(!(MD5Util.MD5(str).equals(Main.this.str[1]))) {
					JOptionPane.showMessageDialog(find, "�𰸴�����������д!");
					return;
				} else {
					find.dispose();
					showFindUI3();
				}
				
				
			}
		});

		find.setResizable(false);
		find.setVisible(true);
		find.setLocationRelativeTo(null);
		find.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}

	/**
	 * �һ��������һ��UI
	 */
	protected void showFindUI3() {
		final JFrame find = new JFrame("�һ�����");
		JLabel l2 = new JLabel("������������:");
		JLabel l3 = new JLabel("��ȷ��������:");
		final JPasswordField p1 = new JPasswordField();
		final JPasswordField p2 = new JPasswordField();
		JButton btn = new JButton("ȷ���޸�");
		// ���ñ���ͼƬ
		ImageIcon bg_icon = new ImageIcon("./images/reg_bg.jpg");
		JLabel l1 = new JLabel(bg_icon);
		l1.setBounds(0,0,300,200);
		find.setBounds(0, 0, 300,200);
		// �����ݴ���ת��ΪJPanel���������÷���setOpaque()��ʹ���ݴ���͸��
		JPanel imgPanel = (JPanel) find.getContentPane();
		imgPanel.setOpaque(false);
		find.getLayeredPane().setLayout(null);
		find.getLayeredPane().add(l1, new Integer(Integer.MIN_VALUE));
		// ����imagePane�Ĳ��ַ�ʽΪ���Բ���
		imgPanel.setLayout(null);
		// ���ñ���ͷ��
		ImageIcon icon = new ImageIcon("./images/test.jpg");
		Image logo = icon.getImage();
		find.setIconImage(logo);
		
		
		l2.setBounds(20,30,140,25);
		p1.setBounds(130,30,90,25);
		btn.setBounds(85,120,100,35);
		l3.setBounds(20,65,140,25);
		p2.setBounds(130,65,90,25);
		
		
		//������
		find.add(l2);
		find.add(p1);
		find.add(btn);
		find.add(l3);
		find.add(p2);
		
		
		//��Ӽ����¼�
		btn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String pwd1 = p1.getText();
				String pwd2 = p2.getText();
				if(pwd1.equals("")) {
					JOptionPane.showMessageDialog(find, "�����벻��Ϊ��!");
					return;
				} else if(!pwd1.equals(pwd2)) {
					JOptionPane.showMessageDialog(find, "�������벻һ��!");
					p1.setText("");
					p2.setText("");
					return;
				} else {
					byte state = conn.changePwd(jknum, pwd1.trim());
					if(state == 1) {
						JOptionPane.showMessageDialog(find, "�޸ĳɹ�!");
						find.dispose();
						return;
					} else {
						JOptionPane.showMessageDialog(find, "�޸�ʧ��!");
						find.dispose();
						return;
					}
				}
			}
		});

		find.setResizable(false);
		find.setVisible(true);
		find.setLocationRelativeTo(null);
		find.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}

	// ��½�¼�����
	private void loginAction() throws FileNotFoundException {
		// 1.ȡ�������jk�ź�����
		String jkStr = username.getText().trim();
		Pattern pattern = Pattern.compile("[0-9]*");
		Matcher isNum = pattern.matcher(jkStr);
		if (!isNum.matches()) {
			JOptionPane.showMessageDialog(jf_login, "��½ʧ��,QQ�������������!");
			username.setText("");
			pwd.setText("");
			return;
		}

		int jkNum = Integer.parseInt(jkStr);
		String password = pwd.getText().trim();
		int state = com1.getSelectedIndex() + 1;

		// �ж��Ƿ��Ѿ���½
		File loginstate = new File("F:/QQmsg/loginstate.dat");
		if (loginstate.exists()) {
			ArrayList<Integer> list;
			try {
				ObjectInputStream oins = new ObjectInputStream(
						new FileInputStream(loginstate));
				list = (ArrayList<Integer>) oins.readObject();
				if (list.indexOf((Integer) jkNum) != -1) {
					JOptionPane.showMessageDialog(jf_login, "�ѵ�½���ʺţ������ظ���½!");
					this.username.setText("");
					this.pwd.setText("");
					return;
				}
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}

		// 2.�����Ϸ�����
		if (conn.conn2Server()) {// ����������Ϸ�����
			// 3.��½
			Jkuser jkuser = null;
			if ((jkuser = conn.loginServer(jkNum, password, state)) != null) {

				// ״̬�ļ����л�������
				File stateFile = new File("F:/QQmsg/state.dat");
				if (!stateFile.getParentFile().exists()) {
					stateFile.getParentFile().mkdirs();
				}
				PrintWriter pw = new PrintWriter(stateFile);
				boolean flag1 = c1.isSelected();
				boolean flag2 = c2.isSelected();
				if (!flag1 && !flag2) {
					pw.println(0);
					pw.println(0);
				} else if (!flag1 && flag2) {
					pw.println(0);
					pw.println(1);
				} else if (flag1 && !flag2) {
					pw.println(1);
					pw.println(0);
				} else if (flag1 && flag2) {
					pw.println(1);
					pw.println(1);
				}
				pw.flush();
				pw.close();
				// һ��½ֱ�Ӱ�user�������л����浽����
				if (flag1) {
					jkuser.setPassword(pwd.getText());
				}

				File file = new File("F:/QQmsg/user.dat");
				if (!file.getParentFile().exists()) {
					file.getParentFile().mkdirs();
				}
				ObjectOutputStream oos = null;
				try {
					oos = new ObjectOutputStream(new FileOutputStream(file));
					oos.writeObject(jkuser);
				} catch (IOException e) {
					e.printStackTrace();
				}

				jf_login.dispose();
				// 4.��ʾ���������� //��½�ɹ��ˣ�Ҫ�ص���½����
				MainUI mainUI = new MainUI(jkuser);
				mainUI.showMainUI();
				// 5.���������߳�
				conn.start();
				// 6.���û����Ӹ��������,��Ϊ��Ϣ������
				conn.addMsgListener(mainUI);
				// ����״̬�ļ�
				if (loginstate.exists()) {
					try {
						ObjectInputStream oins = new ObjectInputStream(
								new FileInputStream(loginstate));
						ArrayList<Integer> loginList = (ArrayList<Integer>) oins
								.readObject();
						loginList.add(jkNum);
						// ����д��ȥ
						ObjectOutputStream oos3 = new ObjectOutputStream(
								new FileOutputStream(loginstate));
						oos3.writeObject(loginList);
						oos3.flush();
					} catch (IOException e) {
						e.printStackTrace();
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					}
				} else {
					List<Integer> list = new ArrayList<Integer>();
					list.add(jkNum);
					try {
						ObjectOutputStream oos2 = new ObjectOutputStream(
								new FileOutputStream(loginstate));
						oos2.writeObject(list);
						oos2.flush();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			} else {
				conn.closeMe();
				JOptionPane.showMessageDialog(jf_login, "��½ʧ��,��ȷ���ʺ���ȷ!");
				username.setText("");
				pwd.setText("");
			}
		} else {
			conn.closeMe();
			JOptionPane.showMessageDialog(jf_login, "����ʧ��,��ȷ�Ϸ���������,IP�Ͷ˿���ȷ!");
		}
	}

	/**
	 * ��ʾע��ҳ��
	 */
	private void showRegUI() {
		final JFrame jf_reg = new JFrame("QQע�����");
		jf_reg.setLayout(null);
		jf_reg.setBounds(0, 0, 425, 700);

		// ע������õ����������

		// ��ǩ
		JLabel jl1; // �û��ǳ�
		JLabel jl2; // ����
		JLabel jl3; // �����ظ�
		JLabel jl4; // �û��Ա�
		JLabel jl5; // �û�����
		JLabel jl6; // �ֻ�����
		JLabel jl7; // �û����ڵ�
		JLabel jl8; // ����ǩ��
		JLabel jl9; // �ܱ�����
		JLabel jl10; // �ܱ������
		final JLabel jl11; // �û��ǳ���ʾ��Ϣ
		final JLabel jl12; // ������ʾ��Ϣ
		final JLabel jl13; // �����ظ���ʾ��Ϣ
		final JLabel jl14; // �û��Ա���ʾ��Ϣ
		final JLabel jl15; // �û�������ʾ��Ϣ
		final JLabel jl16; // �ֻ�������ʾ��Ϣ
		final JLabel jl17; // �û����ڵ���ʾ��Ϣ
		final JLabel jl18; // ����ǩ����ʾ��Ϣ
		final JLabel jl19; // �ܱ�������ʾ��Ϣ
		final JLabel jl20; // �ܱ��������ʾ��Ϣ

		// ȷ��ע�ᰴť
		JButton submit;

		// �ı���������
		final JTextField jt1; // �û��ǳ�����
		final JComboBox jt2; // �Ա�����
		final JTextField jt3; // ����
		final JTextField jt4; // �ֻ�����
		final JTextField jt5; // �û����ڵ�����
		final JTextArea jt6; // ����ǩ������
		final JTextField jt7; // �ܱ���������
		final JTextField jt8; // �ܱ�������
		final JPasswordField jp1; // ��������
		final JPasswordField jp2; // �����ظ�����

		// ���ñ���ͷ��
		ImageIcon icon = new ImageIcon("./images/logo.jpg");
		Image logo = icon.getImage();
		jf_reg.setIconImage(logo);
		// ���ñ���ͼƬ
		ImageIcon bg_icon = new ImageIcon("./images/reg_bg.jpg");
		JLabel jl_bg = new JLabel(bg_icon);
		jl_bg.setBounds(0, 0, bg_icon.getIconWidth(), bg_icon.getIconHeight());
		// �����ݴ���ת��ΪJPanel���������÷���setOpaque()��ʹ���ݴ���͸��
		JPanel imgPanel = (JPanel) jf_reg.getContentPane();
		imgPanel.setOpaque(false);
		jf_reg.getLayeredPane().setLayout(null);
		jf_reg.getLayeredPane().add(jl_bg, new Integer(Integer.MIN_VALUE));
		// ����imagePane�Ĳ��ַ�ʽΪ���Բ���
		imgPanel.setLayout(null);
		// ImagePane������ĳ�ʼ��
		jl1 = new JLabel("�û��ǳ�(����):");
		jl1.setBounds(40, 40, 100, 25);
		jt1 = new JTextField();
		jt1.setBounds(150, 40, 130, 30);
		jl11 = new JLabel("");
		jl11.setForeground(Color.red);
		jl11.setBounds(300, 40, 100, 25);
		jl2 = new JLabel("��¼����(����):");
		jl2.setBounds(40, 80, 100, 25);
		jp1 = new JPasswordField();
		jp1.setBounds(150, 80, 130, 30);
		jl12 = new JLabel("");
		jl12.setForeground(Color.red);
		jl12.setBounds(300, 80, 100, 25);
		jl3 = new JLabel("ȷ������(����):");
		jl3.setBounds(40, 120, 100, 25);
		jp2 = new JPasswordField();
		jp2.setBounds(150, 120, 130, 30);
		jl13 = new JLabel("");
		jl13.setForeground(Color.red);
		jl13.setBounds(300, 120, 100, 25);
		jl4 = new JLabel("�����Ա�:");
		jl4.setBounds(40, 160, 100, 25);
		jt2 = new JComboBox();
		jt2.addItem("-----------------------");
		jt2.addItem("��");
		jt2.addItem("Ů");
		jt2.setBounds(150, 160, 130, 30);
		jl14 = new JLabel("");
		jl14.setForeground(Color.red);
		jl14.setBounds(300, 160, 100, 25);
		jl5 = new JLabel("��������:");
		jl5.setBounds(40, 200, 100, 25);
		jt3 = new JTextField();
		jt3.setBounds(150, 200, 130, 30);
		jl15 = new JLabel("");
		jl15.setForeground(Color.red);
		jl15.setBounds(300, 200, 100, 25);
		jl6 = new JLabel("�����ֻ�����:");
		jl6.setBounds(40, 240, 100, 25);
		jt4 = new JTextField();
		jt4.setBounds(150, 240, 130, 30);
		jl16 = new JLabel("");
		jl16.setForeground(Color.red);
		jl16.setBounds(300, 240, 100, 25);
		jl7 = new JLabel("�������ڵ�:");
		jl7.setBounds(40, 280, 100, 25);
		jt5 = new JTextField();
		jt5.setBounds(150, 280, 130, 30);
		jl17 = new JLabel("");
		jl17.setForeground(Color.red);
		jl17.setBounds(300, 280, 100, 25);
		jl8 = new JLabel("���ĸ���ǩ��:");
		jl8.setBounds(40, 320, 100, 25);
		jt6 = new JTextArea();
		jt6.setBounds(150, 320, 130, 140);
		jl18 = new JLabel("");
		jl18.setForeground(Color.red);
		jl18.setBounds(300, 320, 100, 25);
		jl9 = new JLabel("�ܱ�����(����):");
		jl9.setBounds(40, 475, 100, 25);
		jt7 = new JTextField();
		jt7.setBounds(150, 475, 130, 30);
		jl19 = new JLabel("");
		jl19.setForeground(Color.red);
		jl19.setBounds(300, 475, 100, 25);
		jl10 = new JLabel("�ܱ���(����):");
		jl10.setBounds(40, 515, 100, 25);
		jt8 = new JTextField();
		jt8.setBounds(150, 515, 130, 30);
		jl20 = new JLabel("");
		jl20.setForeground(Color.red);
		jl20.setBounds(300, 515, 100, 25);
		submit = new JButton();
		ImageIcon reg_icon = new ImageIcon("./images/reg.jpg");
		submit.setIcon(reg_icon);
		submit.setBounds(150, 580, reg_icon.getIconWidth(),
				reg_icon.getIconHeight());
		submit.setContentAreaFilled(false);

		// ��imagePane��������
		imgPanel.add(jl1);
		imgPanel.add(jt1);
		imgPanel.add(jl11);
		imgPanel.add(jl2);
		imgPanel.add(jp1);
		imgPanel.add(jl12);
		imgPanel.add(jl3);
		imgPanel.add(jp2);
		imgPanel.add(jl13);
		imgPanel.add(jl4);
		imgPanel.add(jt2);
		imgPanel.add(jl14);
		imgPanel.add(jl5);
		imgPanel.add(jt3);
		imgPanel.add(jl15);
		imgPanel.add(jl6);
		imgPanel.add(jt4);
		imgPanel.add(jl16);
		imgPanel.add(jl7);
		imgPanel.add(jl17);
		imgPanel.add(jt5);
		imgPanel.add(jl8);
		imgPanel.add(jt6);
		imgPanel.add(jl18);
		imgPanel.add(jl9);
		imgPanel.add(jt7);
		imgPanel.add(jl19);
		imgPanel.add(jl10);
		imgPanel.add(jt8);
		imgPanel.add(jl20);
		imgPanel.add(submit);

		// ע�ᰴť�ļ���
		submit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// ������Ҫ���һЩ�ͻ��˵���֤����
				String name = jt1.getText().trim();
				String password = jp1.getText().trim();
				String password2 = jp2.getText().trim();
				String sex = (String) jt2.getItemAt(jt2.getSelectedIndex());
				String email = jt3.getText().trim();
				String phone = jt4.getText().trim();
				String site = jt5.getText().trim();
				String signature = jt6.getText().trim();
				String question = jt7.getText().trim();
				String answer = jt8.getText().trim();

				boolean flag1 = Pattern
						.matches(
								"^\\s*\\w+(?:\\.{0,1}[\\w-]+)*@[a-zA-Z0-9]+(?:[-.][a-zA-Z0-9]+)*\\.[a-zA-Z]+\\s*$",
								email);
				boolean flag2 = Pattern
						.matches(
								"1([\\d]{10})|((\\+[0-9]{2,4})?\\(?[0-9]+\\)?-?)?[0-9]{7,8}",
								phone);

				if (name.equals("") || name == null) {
					jl11.setText("�ǳƲ���Ϊ��");
					jl12.setText("");
					jl13.setText("");
					jl19.setText("");
					jl20.setText("");
					jp1.setText("");
					jp2.setText("");
					jl15.setText("");
					jl16.setText("");
					return;
				} else if (password.equals("") || password == null) {
					jl12.setText("���벻��Ϊ��");
					jl11.setText("");
					jl13.setText("");
					jl19.setText("");
					jl20.setText("");
					jp1.setText("");
					jp2.setText("");
					jl15.setText("");
					jl16.setText("");
					return;
				} else if (!password.equals(password2)) {
					jl13.setText("�������벻ƥ��");
					jl11.setText("");
					jl12.setText("");
					jl19.setText("");
					jl20.setText("");
					jp1.setText("");
					jp2.setText("");
					jl15.setText("");
					jl16.setText("");
					return;
				} else if (!email.equals("") && !flag1) {
					jl15.setText("���䲻�Ϸ�");
					jl11.setText("");
					jl12.setText("");
					jl13.setText("");
					jl16.setText("");
					jl19.setText("");
					jl20.setText("");
					jp1.setText("");
					jp2.setText("");
					return;
				} else if (!phone.equals("") && !flag2) {
					jl16.setText("�ֻ��Ų��Ϸ�");
					jl11.setText("");
					jl12.setText("");
					jl13.setText("");
					jl15.setText("");
					jl19.setText("");
					jl20.setText("");
					jp1.setText("");
					jp2.setText("");
					return;
				} else if (question.equals("") || question == null) {
					jl19.setText("�ܱ�����δ����");
					jl11.setText("");
					jl12.setText("");
					jl13.setText("");
					jl20.setText("");
					jp1.setText("");
					jp2.setText("");
					jl15.setText("");
					jl16.setText("");
					return;
				} else if (answer.equals("") || answer == null) {
					jl20.setText("�ܱ���δ����");
					jl11.setText("");
					jl12.setText("");
					jl13.setText("");
					jl19.setText("");
					jp1.setText("");
					jp2.setText("");
					jl15.setText("");
					jl16.setText("");
					return;
				}
				System.out.println("start��" + answer);
				// ��֤����� ��ʼ�����������ע������ ����jknum����ֵΪ0 �����˷��������ٽ������·���
				Jkuser jkuser = new Jkuser(0, name, password2, signature, null,
						site, phone, email, 0, question, answer, sex);
				System.out.println("next��" + jkuser.getAnswer());
				String s = "����������ʧ��!";
				if (ClientConnection.getIns().conn2Server()) {
					try {
						int jknum = ClientConnection.getIns().regServer(jkuser);
						if (jknum == -1) {
							s = "ע��ʧ��,��ʶ��:" + jknum;
						} else {
							s = "ע��ɹ�,���JK��:" + jknum;
						}
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
				JOptionPane.showMessageDialog(jf_reg, s);
				conn.closeMe();
				jf_reg.dispose();

			}
		});

		jf_reg.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				jf_reg.dispose();
			}
		});

		jf_reg.setResizable(false);
		jf_reg.setLocationRelativeTo(null);
		jf_reg.setVisible(true);
	}

	/**
	 * �ͻ��˵Ŀ���
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		Main main = new Main();
		main.showLoginUI();
	}

}
