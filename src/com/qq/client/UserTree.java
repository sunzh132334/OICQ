package com.qq.client;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.Point;
import java.awt.PopupMenu;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.xml.soap.Node;

import com.qq.model.Jkgroup;
import com.qq.model.Jkuser;
import com.qq.msg.IMsgConstance;
import com.qq.msg.MsgAddGroup;
import com.qq.msg.MsgChatFile;
import com.qq.msg.MsgChatText;
import com.qq.msg.MsgDeleteFriendResp;
import com.qq.msg.MsgDeleteGroup;
import com.qq.msg.MsgHead;
import com.qq.util.ImageUtil;
import com.qq.util.OnLineInfoUtil;

/**
 * QQͨ��ϵͳ ������������ʾ����/�����б�����νṹ �����������Ϣ
 * 
 * @author yy
 */
public class UserTree extends JTree {

	private static final long serialVersionUID = 1L;
	private Map<Integer, Integer> sendFrameMap = new HashMap<Integer, Integer>();// <jknum,times>mapping
	private Object userO = null;
	private Jkuser jkuser; // �û�����
	Point p = null;
	private UserTree ut = null;

	private List<JFrame> jfList = new ArrayList<JFrame>();
	private List<JTextArea> jtList = new ArrayList<JTextArea>();
	private Map<Integer, Integer> map = new HashMap<Integer, Integer>();
	DefaultMutableTreeNode root = null;
	// ȡ���������ĵ�ʵ������
	private ClientConnection conn = ClientConnection.getIns();
	String input = null;
	// ������ʱ������jkuser����
	public UserTree(Jkuser jkuser) {
		this.jkuser = jkuser;
		go();
		ut = this;
	}

	private void go() {
		root = new DefaultMutableTreeNode(new NodeData(3, "���ѹ���"));
		DefaultTreeModel tm = new DefaultTreeModel(root);
		tm.setAsksAllowsChildren(true);
		// ��ӷ���
		List<Jkgroup> glist = jkuser.getGroupList();
		for (int i = 0; i < glist.size(); i++) {
			// ��ӷ������ȫ������
			List<Jkuser> ulist = glist.get(i).getUserList();
			List<Jkuser> onUlist = new ArrayList<Jkuser>(); // ���ߺ����б�
			List<Jkuser> offUlist = new ArrayList<Jkuser>(); // �������б�
			for (int j = 0; j < ulist.size(); j++) {
				if (ulist.get(j).getState() == 1) {
					onUlist.add(ulist.get(j));
				} else {
					offUlist.add(ulist.get(j));
				}
			}
			DefaultMutableTreeNode gnode = new DefaultMutableTreeNode(
					new NodeData(0, glist.get(i), onUlist.size(), ulist.size()));
			root.add(gnode);
			for (int j = 0; j < onUlist.size(); j++) {
				DefaultMutableTreeNode unode = new DefaultMutableTreeNode(
						new NodeData(2, onUlist.get(j)));
				unode.setAllowsChildren(false);
				gnode.add(unode);
			}
			for (int j = 0; j < offUlist.size(); j++) {
				DefaultMutableTreeNode unode = new DefaultMutableTreeNode(
						new NodeData(4, offUlist.get(j)));
				unode.setAllowsChildren(false);

				gnode.add(unode);
			}

		}

		this.setCellRenderer(new MyRender());

		// ���ü������ڵ�ı���ͼƬ
		DefaultTreeCellRenderer render = (DefaultTreeCellRenderer) this
				.getCellRenderer();
		ImageIcon openIcon = new ImageIcon("./images/list_open.png");
		ImageIcon closeIcon = new ImageIcon("./images/list_close.png");
		render.setOpenIcon(openIcon);
		render.setClosedIcon(closeIcon);

		this.setRootVisible(false);
		this.putClientProperty("JTree.lineStyle", "None");// ȥ��������
		this.setModel(tm);

		final JPopupMenu menu = new JPopupMenu();
		JMenuItem deleteFriend = new JMenuItem("ɾ������");
		menu.add(deleteFriend);
		JMenuItem selectFriend = new JMenuItem("�鿴������Ϣ");
		menu.add(selectFriend);

		final JPopupMenu menu2 = new JPopupMenu();
		JMenuItem deleteGroup = new JMenuItem("ɾ������");
		menu2.add(deleteGroup);

		final JPopupMenu menu3 = new JPopupMenu();
		JMenuItem addNewGroup = new JMenuItem("�����·���");
		menu3.add(addNewGroup);

		// ��������Mouse�¼���������˫������������û�����Ϣ
		this.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {// ˫���¼�
					showSendFrame();// ����������Ϣ��
				}
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				if (e.isPopupTrigger()) {
					if (e.getX() > 210) {
						menu3.show(ut, e.getX(), e.getY());
						return;
					}
					// �õ�����ѡ�еĽڵ�:
					TreePath tp = ut.getSelectionPath();
					;
					if (tp == null) {// δѡ�����ڵ�
						return;
					}

					Object obj = tp.getLastPathComponent();// ȡ��ѡ�еĽڵ�
					DefaultMutableTreeNode node = (DefaultMutableTreeNode) obj;
					userO = node.getUserObject();// ȡ�ýڵ��ڵĶ���
					if (userO instanceof NodeData
							&& (((NodeData) userO).nodeType == 2 || ((NodeData) userO).nodeType == 4)) {
						menu.show(ut, e.getX(), e.getY());
					} else if (userO instanceof NodeData
							&& ((NodeData) userO).nodeType == 0) {
						NodeData data = (NodeData) userO;
						Jkgroup group = (Jkgroup) data.value;
						if (group.getName().trim().equals("�ҵĺ���")) {
							return;
						}
						menu2.show(ut, e.getX(), e.getY());
					}
				}
			}
		});

		addNewGroup.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				input = JOptionPane.showInputDialog(ut, "�������������").trim();
				List<Jkgroup> gList = jkuser.getGroupList();
				boolean flag = false;

				for (int i = 0; i < gList.size(); i++) {
					if (gList.get(i).getName().equals(input)) {
						flag = true;
						break;
					}
				}

				if (flag) {
					JOptionPane.showMessageDialog(ut, "�������Ѵ���!");
				} else {

					MsgAddGroup addGroup = new MsgAddGroup();
					addGroup.setTotalLength(269);
					addGroup.setType(IMsgConstance.command_addGroup);
					addGroup.setDest(IMsgConstance.Server_JK_NUMBER);
					addGroup.setSrc(jkuser.getJknum());
					addGroup.setGroupName(input);

					try {
						conn.sendMsg(addGroup);
					} catch (Exception e1) {
						e1.printStackTrace();
					}

				}

			}
		});
		deleteFriend.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if ((userO instanceof NodeData)
						&& (((NodeData) userO).nodeType == 2 || ((NodeData) userO).nodeType == 4)) {// ѡ�е���һ���û��ڵ����
					NodeData data = (NodeData) userO;
					final Jkuser user = (Jkuser) data.value;
					// ����������Ϣ��

					int n = JOptionPane.showConfirmDialog(ut,
							"��ȷ��Ҫɾ��" + user.getName() + "(" + user.getJknum()
									+ ")" + "�� ?");
					if (n == JOptionPane.YES_OPTION) {
						// �����������ɾ����������
						MsgHead head = new MsgHead();
						head.setSrc(jkuser.getJknum());
						head.setDest(user.getJknum());
						head.setType(IMsgConstance.command_deleteFriend);
						head.setTotalLength(13);

						try {
							conn.sendMsg(head);
						} catch (Exception e1) {
							e1.printStackTrace();
						}
					}
				}
			}
		});

		selectFriend.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if ((userO instanceof NodeData)
						&& (((NodeData) userO).nodeType == 2 || ((NodeData) userO).nodeType == 4)) {// ѡ�е���һ���û��ڵ����
					NodeData data = (NodeData) userO;
					final Jkuser user = (Jkuser) data.value;
					// ����������Ϣ��
					JFrame frame = new JFrame("�û�����");
					frame.setLayout(null);
					frame.setBounds(0, 0, 420, 370);
					frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
					frame.setVisible(true);
					frame.setLocationRelativeTo(null);

					// ���ñ���ͷ��
					ImageIcon icon = new ImageIcon("./images/logo.jpg");
					Image logo = icon.getImage();
					frame.setIconImage(logo);
					// ���ñ���ͼƬ
					ImageIcon bg_icon = new ImageIcon("./images/reg_bg.jpg");
					JLabel jl_bg = new JLabel(bg_icon);
					jl_bg.setBounds(0, 0, bg_icon.getIconWidth(),
							bg_icon.getIconHeight());
					// �����ݴ���ת��ΪJPanel���������÷���setOpaque()��ʹ���ݴ���͸��
					JPanel imgPanel = (JPanel) frame.getContentPane();
					imgPanel.setOpaque(false);
					frame.getLayeredPane().setLayout(null);
					frame.getLayeredPane().add(jl_bg,
							new Integer(Integer.MIN_VALUE));
					// ����imagePane�Ĳ��ַ�ʽΪ���Բ���
					imgPanel.setLayout(null);

					JLabel jl_jknum = new JLabel("QQ����:");
					jl_jknum.setBounds(40, 20, 60, 30);
					JLabel jl_jknum2 = new JLabel(user.getJknum() + "");
					jl_jknum2.setBounds(110, 20, 85, 30);
					JLabel jl_name = new JLabel("�ǳ�:");
					jl_name.setBounds(40, 60, 60, 30);
					JLabel jl_name2 = new JLabel(user.getName());
					jl_name2.setBounds(110, 60, 260, 30);
					JLabel jl_sign = new JLabel("����ǩ��:");
					jl_sign.setBounds(40, 100, 60, 30);
					JLabel jl_sign2 = new JLabel(user.getSignature());
					jl_sign2.setBounds(110, 100, 260, 30);
					JLabel jl_site = new JLabel("�ص�:");
					jl_site.setBounds(40, 140, 60, 30);
					JLabel jl_site2 = new JLabel(user.getSite());
					jl_site2.setBounds(110, 140, 85, 30);
					JLabel jl_phone = new JLabel("�绰:");
					jl_phone.setBounds(40, 180, 60, 30);
					JLabel jl_phone2 = new JLabel(user.getPhone());
					jl_phone2.setBounds(110, 180, 85, 30);
					JLabel jl_email = new JLabel("����:");
					jl_email.setBounds(40, 220, 60, 30);
					JLabel jl_email2 = new JLabel(user.getEmail());
					jl_email2.setBounds(110, 220, 260, 30);
					JLabel jl_sex = new JLabel("�Ա�:");
					jl_sex.setBounds(40, 260, 60, 30);
					String sex = (user.getSex() == 0) ? "��" : "Ů";
					JLabel jl_sex2 = new JLabel(sex);
					jl_sex2.setBounds(110, 260, 85, 30);

					// ������
					frame.add(jl_jknum2);
					frame.add(jl_jknum);
					frame.add(jl_name);
					frame.add(jl_name2);
					frame.add(jl_sign);
					frame.add(jl_sign2);
					frame.add(jl_site);
					frame.add(jl_site2);
					frame.add(jl_phone2);
					frame.add(jl_phone);
					frame.add(jl_email2);
					frame.add(jl_email);
					frame.add(jl_sex2);
					frame.add(jl_sex);
				}
			}
		});

		deleteGroup.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				NodeData data = (NodeData) userO;
				Jkgroup group = (Jkgroup) data.value;
				int n = JOptionPane.showConfirmDialog(ut,
						"��ȷ��Ҫɾ������  '" + group.getName() + "'  ��?");
				if (n == JOptionPane.YES_OPTION) {
					MsgDeleteGroup deleteGroup = new MsgDeleteGroup();
					deleteGroup.setTotalLength(17);
					deleteGroup.setType(IMsgConstance.command_deleteGroup);
					deleteGroup.setSrc(jkuser.getJknum());
					deleteGroup.setDest(IMsgConstance.Server_JK_NUMBER);
					deleteGroup.setGid(group.getGid());

					try {
						conn.sendMsg(deleteGroup);
					} catch (Exception e1) {
						e1.printStackTrace();
					}

				}
			}
		});

	}

	/**
	 * ����ͻ��˽��յ�һ����Ϣʱ�Ķ���,�������洰�������� 1.��½�ɹ��󣬽��յ����ѷ��� 2.���һ�����ѳɹ� 3.�ӵ�������Ϣ
	 * 4.�ӵ��ļ�������Ϣ
	 * 
	 * @param m
	 *            :���յ�����Ϣ����
	 */
	public void onMsgRecive(MsgHead m) {
		if (m.getType() == IMsgConstance.command_chatText) {
			MsgChatText chatText = (MsgChatText) m;
			if (map.get(m.getSrc()) != null) {
				int index = map.get(m.getSrc());
				JFrame jf_send = jfList.get(index);
				JTextArea chatArea = jtList.get(index);
				jf_send.setExtendedState(JFrame.NORMAL);
				String sendTime = chatText.getSendTime();
				sendTime = sendTime.substring(11);
				int len1 = sendTime.getBytes().length;
				String space1 = "";
				for (int i = 0; i < 3; i++) {
					space1 += "  ";
				}
				String txt = chatText.getCharTxt();
				int len2 = txt.getBytes().length;
				String space2 = "";
				for (int i = 0; i < 3; i++) {
					space2 += "  ";
				}
				chatArea.setText(chatArea.getText() + "\n" + space1 + sendTime
						+ "\n" + space2 + txt + "\n");
			} else {
				// ���Ȼ�÷��ͷ��Ļ�����Ϣ
				int jknum = chatText.getSrc();
				Jkuser src = null;
				ArrayList<Jkgroup> gList = (ArrayList<Jkgroup>) jkuser
						.getGroupList();
				boolean flag = false;
				for (int i = 0; i < gList.size(); i++) {
					Jkgroup jkgroup = gList.get(i);
					ArrayList<Jkuser> uList = (ArrayList<Jkuser>) jkgroup
							.getUserList();
					for (int j = 0; j < uList.size(); j++) {
						if (uList.get(j).getJknum() == jknum) {
							flag = true;
							src = uList.get(j);
							break;
						}
					}
					if (flag)
						break;
				}
				showSendMsgUI(src);
				jfList.get(map.get(jknum)).setExtendedState(JFrame.NORMAL);
				String sendTime = chatText.getSendTime();
				sendTime = sendTime.substring(11);
				int len1 = sendTime.getBytes().length;
				String space1 = "";
				for (int i = 0; i < 3; i++) {
					space1 += "  ";
				}
				String txt = chatText.getCharTxt();
				int len2 = txt.getBytes().length;
				String space2 = "";
				for (int i = 0; i < 3; i++) {
					space2 += "  ";
				}
				jtList.get(map.get(jknum)).setText(
						space1 + sendTime + "\n" + space2 + txt + "\n");
			}
		} else if (m.getType() == IMsgConstance.command_onLine) {
			int onNum = m.getSrc();
			OnLineInfoUtil infoUtil = new OnLineInfoUtil();
			infoUtil.show("QQ������������", "���ĺ��� " + onNum + " �Ѿ�����");
			updateUserState(onNum, 1);
			go();
		} else if (m.getType() == IMsgConstance.command_offLine) {
			int offNum = m.getSrc();
			OnLineInfoUtil infoUtil = new OnLineInfoUtil();
			infoUtil.show("QQ������������", "���ĺ��� " + offNum + " �Ѿ�����");
			updateUserState(offNum, 2);
			go();
		} else if (m.getType() == IMsgConstance.command_chatFile) {
			MsgChatFile chatFile = (MsgChatFile) m;
			int srcNum = chatFile.getSrc();
//			if (sendFrameMap.get(srcNum) == null) {
			if(map.get(srcNum) == null) {
				Jkuser srcUser = getUserByJknum(srcNum);
				showSendMsgUI(srcUser);
				String sendTime = chatFile.getSendTime();
				sendTime = srcUser.getName() + "(" + srcUser.getJknum() + ")"
						+ sendTime.substring(11);
				int len1 = sendTime.getBytes().length;
				String space1 = "";
				for (int i = 0; i < 3; i++) {
					space1 += "  ";
				}
				String txt = "�����ļ� " + chatFile.getFileName();
				int len2 = txt.getBytes().length;
				String space2 = "";
				for (int i = 0; i < 3; i++) {
					space2 += "  ";
				}
				jtList.get(map.get(srcNum)).setText(
						space1 + sendTime + "\n" + space2 + txt + "\n");
				String name = chatFile.getFileName();
				int n = JOptionPane.showConfirmDialog(
						jfList.get(map.get(srcUser.getJknum())), "�Ƿ����"
								+ chatFile.getSrc() + "�������ļ�" + name);
				if (n == JOptionPane.YES_OPTION) {
					JFileChooser chooser = new JFileChooser();
					chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
					File file = new File("G:/");
					chooser.setCurrentDirectory(file);
					int num = chooser.showSaveDialog(chooser);
					if (num == chooser.APPROVE_OPTION) {
						String path = chooser.getSelectedFile().toString()
								+ "/" + chatFile.getFileName();
						BufferedOutputStream bous = null;
						try {
							bous = new BufferedOutputStream(
									new FileOutputStream(path));
							bous.write(chatFile.getFileData());
							bous.flush();
							System.out.println(chatFile.getFileData());
						} catch (FileNotFoundException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						} finally {
							if (bous != null) {
								try {
									bous.close();
								} catch (IOException e) {
									e.printStackTrace();
								}
							}
						}
					}
				}
			} else {
				Jkuser srcUser = getUserByJknum(srcNum);
				String sendTime = chatFile.getSendTime();
				sendTime = srcUser.getName() + "(" + srcUser.getJknum() + ")"
						+ sendTime.substring(11);
				int len1 = sendTime.getBytes().length;
				String space1 = "";
				for (int i = 0; i < 3; i++) {
					space1 += "  ";
				}
				String txt = "�����ļ� " + chatFile.getFileName();
				int len2 = txt.getBytes().length;
				String space2 = "";
				for (int i = 0; i < 3; i++) {
					space2 += "  ";
				}
				jtList.get(map.get(srcNum)).setText(
						space1 + sendTime + "\n" + space2 + txt + "\n");
				String name = chatFile.getFileName();
				int n = JOptionPane.showConfirmDialog(
						jfList.get(map.get(srcUser.getJknum())), "�Ƿ����"
								+ chatFile.getSrc() + "�������ļ�" + name);
				if (n == JOptionPane.YES_OPTION) {
					JFileChooser chooser = new JFileChooser();
					chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
					File file = new File("G:/");
					chooser.setCurrentDirectory(file);
					int num = chooser.showSaveDialog(chooser);
					if (num == chooser.APPROVE_OPTION) {
						String path = chooser.getSelectedFile().toString()
								+ "/" + chatFile.getFileName();
						BufferedOutputStream bous = null;
						try {
							bous = new BufferedOutputStream(
									new FileOutputStream(path));
							bous.write(chatFile.getFileData());
							bous.flush();
							System.out.println(chatFile.getFileData());
						} catch (FileNotFoundException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						} finally {
							if (bous != null) {
								try {
									bous.close();
								} catch (IOException e) {
									e.printStackTrace();
								}
							}
						}
					}
				}
			}
		}
	}

	/**
	 * ����jknum���ĳ�����ѵĻ�����Ϣ
	 * 
	 * @param srcNum
	 * @return
	 */
	private Jkuser getUserByJknum(int srcNum) {
		Jkuser u = null;
		boolean flag = true;
		ArrayList<Jkgroup> gList = (ArrayList<Jkgroup>) jkuser.getGroupList();
		for (int i = 0; i < gList.size(); i++) {
			ArrayList<Jkuser> uList = (ArrayList<Jkuser>) gList.get(i)
					.getUserList();
			for (int j = 0; j < uList.size(); j++) {
				if (uList.get(j).getJknum() == srcNum) {
					u = uList.get(j);
					flag = false;
					break;
				}
			}
			if (!flag)
				break;
		}
		return u;
	}

	/**
	 * ����ĳ���û���״̬
	 * 
	 * @param jknum
	 * @param state
	 */
	private void updateUserState(int jknum, int state) {
		ArrayList<Jkgroup> glist = (ArrayList<Jkgroup>) jkuser.getGroupList();
		boolean flag = false;
		for (int i = 0; i < glist.size(); i++) {
			ArrayList<Jkuser> ulist = (ArrayList<Jkuser>) glist.get(i)
					.getUserList();
			for (int j = 0; j < ulist.size(); j++) {
				Jkuser user = ulist.get(j);
				if (user.getJknum() == jknum) {
					user.setState(state);
					flag = true;
					break;
				}
			}
			if (flag)
				break;
		}
	}

	/* ˫�����ϵĺ��ѽڵ�ʱ��������Ϣ���Ϳ� */
	private void showSendFrame() {
		// �õ�����ѡ�еĽڵ�:
		TreePath tp = this.getSelectionPath();
		if (tp == null) {// δѡ�����ڵ�
			return;
		}
		Object obj = tp.getLastPathComponent();// ȡ��ѡ�еĽڵ�
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) obj;
		userO = node.getUserObject();// ȡ�ýڵ��ڵĶ���
		if ((userO instanceof NodeData)
				&& (((NodeData) userO).nodeType == 2 || ((NodeData) userO).nodeType == 4)) {// ѡ�е���һ���û��ڵ����
			NodeData data = (NodeData) userO;
			final Jkuser destUser = (Jkuser) data.value;
			// ����������Ϣ��
			if (sendFrameMap.get(destUser.getJknum()) == null) {
				showSendMsgUI(destUser);
				sendFrameMap.put(destUser.getJknum(), 1);
			}
		}
	}

	void showSendMsgUI(final Jkuser destUser) {
		final JFrame jf_send = new JFrame("QQ���촰��");
		jf_send.setLayout(null);
		jf_send.setBounds(0, 0, 380, 480);
		// ���ñ���ͷ��
		ImageIcon icon = new ImageIcon("./images/logo.jpg");
		Image logo = icon.getImage();
		jf_send.setIconImage(logo);
		// ���ñ���ͼƬ
		ImageIcon bg_icon = new ImageIcon("./images/send_bg.jpg");
		JLabel jl_bg = new JLabel(bg_icon);
		jl_bg.setBounds(0, 0, bg_icon.getIconWidth(), bg_icon.getIconHeight());
		// �����ݴ���ת��ΪJPanel���������÷���setOpaque()��ʹ���ݴ���͸��
		JPanel imgPanel = (JPanel) jf_send.getContentPane();
		imgPanel.setOpaque(false);
		jf_send.getLayeredPane().setLayout(null);
		jf_send.getLayeredPane().add(jl_bg, new Integer(Integer.MIN_VALUE));
		// ����imagePane�Ĳ��ַ�ʽΪ���Բ���
		imgPanel.setLayout(null);
		// ȥ��������Ŀ
		jf_send.setUndecorated(true);
		jf_send.getRootPane().setWindowDecorationStyle(JRootPane.NONE);
		// �õ������������
		JLabel header;
		final JLabel min;
		final JLabel close;
		JLabel name;
		JLabel star;
		JLabel signature;
		JLabel fileUpload;
		JLabel distance_control;
		final JTextArea chatArea;
		JScrollPane chatPane;
		JLabel font;
		JLabel face;
		final JTextArea sendArea;
		JScrollPane sendPane;
		JButton close_btn;
		JButton send_btn;

		// �����ʵ����
		header = new JLabel();
		ImageIcon headerIcon = new ImageIcon(
				ImageUtil.getBytesFromFile(destUser.getIconpath()));
		header.setIcon(headerIcon);
		header.setBounds(20, 20, 60, 60);
		min = new JLabel();
		close = new JLabel();
		final ImageIcon minIcon = new ImageIcon("./images/min.png");
		final ImageIcon minIcon2 = new ImageIcon("./images/min1.png");
		min.setIcon(minIcon);
		min.setBounds(340, 10, 10, 10);
		final ImageIcon closeIcon = new ImageIcon("./images/close.png");
		final ImageIcon closeIcon2 = new ImageIcon("./images/close1.png");
		close.setIcon(closeIcon);
		close.setBounds(360, 10, 10, 10);
		name = new JLabel(destUser.getName() + "(" + destUser.getJknum() + ")");
		name.setFont(new Font("����", Font.PLAIN, 16));
		name.setBounds(110, 25, 200, 30);
		star = new JLabel();
		ImageIcon starIcon = new ImageIcon("./images/star.jpg");
		star.setIcon(starIcon);
		star.setBounds(110, 55, 30, 30);
		signature = new JLabel(destUser.getSignature());
		signature.setFont(new Font("����", Font.PLAIN, 14));
		signature.setBounds(150, 55, 200, 30);
		fileUpload = new JLabel();
		ImageIcon fileIcon = new ImageIcon("./images/file_upload.png");
		fileUpload.setIcon(fileIcon);
		fileUpload.setBounds(90, 325, 30, 25);
		distance_control = new JLabel();
		ImageIcon distanceIcon = new ImageIcon("./images/distance_control.png");
		distance_control.setIcon(distanceIcon);
		distance_control.setBounds(125, 325, 30, 25);
		chatArea = new JTextArea();
		chatPane = new JScrollPane(chatArea,
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		chatArea.setOpaque(false);
		chatPane.setBounds(20, 95, 340, 225);
		chatArea.setEditable(false);
		font = new JLabel();
		ImageIcon fontIcon = new ImageIcon("./images/font.png");
		font.setIcon(fontIcon);
		font.setBounds(20, 325, 30, 25);
		chatArea.setBorder(BorderFactory.createLineBorder(Color.gray));
		font.setBorder(BorderFactory.createLineBorder(Color.blue));
		face = new JLabel();
		ImageIcon faceIcon = new ImageIcon("./images/face.png");
		face.setIcon(faceIcon);
		face.setBounds(55, 325, 30, 25);
		distance_control.setBorder(BorderFactory.createLineBorder(Color.blue));
		fileUpload.setBorder(BorderFactory.createLineBorder(Color.blue));
		face.setBorder(BorderFactory.createLineBorder(Color.blue));
		sendArea = new JTextArea();
		sendPane = new JScrollPane(sendArea);
		sendPane.setBounds(20, 360, 340, 80);
		sendPane.setBorder(BorderFactory.createLineBorder(Color.gray));
		sendArea.setOpaque(false);
		close_btn = new JButton("�ر�");
		close_btn.setBounds(245, 450, 60, 25);
		send_btn = new JButton("����");
		send_btn.setBounds(310, 450, 60, 25);

		int index = jfList.size();
		jfList.add(jf_send);
		jtList.add(chatArea);
		// ��Ŀ���û�jknum�����indexͨ��map������
		map.put(destUser.getJknum(), index);

		// ��������
		jf_send.add(header);
		jf_send.add(min);
		jf_send.add(close);
		jf_send.add(name);
		jf_send.add(star);
		jf_send.add(signature);
		jf_send.add(fileUpload);
		jf_send.add(distance_control);
		jf_send.add(chatPane);
		jf_send.add(font);
		jf_send.add(face);
		jf_send.add(sendPane);
		jf_send.add(close_btn);
		jf_send.add(send_btn);
		// �¼��ļ���
		min.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				jf_send.setExtendedState(JFrame.ICONIFIED);
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				min.setIcon(minIcon2);
			}

			@Override
			public void mouseExited(MouseEvent e) {
				min.setIcon(minIcon);
			}

		});

		close.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				sendFrameMap.remove(destUser.getJknum());
				jf_send.dispose();

			}

			@Override
			public void mouseEntered(MouseEvent e) {
				close.setIcon(closeIcon2);
			}

			@Override
			public void mouseExited(MouseEvent e) {
				close.setIcon(closeIcon);
			}
		});

		close_btn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				jf_send.dispose();
				// ͬʱ�Ѽ������jframe��jtextarea�Ƴ� ����������Ӧ��mappingӳ��
				jfList.remove(jf_send);
				jtList.remove(chatArea);
				map.remove(destUser.getJknum());
				sendFrameMap.remove(destUser.getJknum());
			}
		});

		send_btn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String txt = sendArea.getText().trim();
				if (txt == null || txt.equals("")) {
					JOptionPane.showMessageDialog(jf_send, "���ܷ��Ϳ���Ϣ");
					return;
				}
				Date date = new Date(System.currentTimeMillis());
				SimpleDateFormat format = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss");
				String sendTime = format.format(date);
				int state = conn.sendMsg2One(jkuser.getJknum(),
						destUser.getJknum(), txt, sendTime);
				if (state == 1) {
					String appendStr = sendTime.substring(11);
					String space1 = "";
					int len1 = appendStr.getBytes().length;
					for (int i = 5; i < 56 - len1; i++) {
						space1 += "  ";
					}
					String appendStr2 = txt;
					String space2 = "";
					int len2 = appendStr2.getBytes().length;
					for (int i = 6; i < 56 - len2; i++) {
						space2 += "  ";
					}
					chatArea.setText(chatArea.getText() + "\n" + space1
							+ appendStr + "\n" + space2 + appendStr2 + "\n");
					sendArea.setText("");
				}
			}
		});

		jf_send.addMouseListener(new MouseAdapter() {

			@Override
			public void mousePressed(MouseEvent e) {
				p = e.getPoint();
			}
		});

		jf_send.addMouseMotionListener(new MouseMotionListener() {

			@Override
			public void mouseMoved(MouseEvent e) {

			}

			@Override
			public void mouseDragged(MouseEvent e) {
				Point p1 = e.getPoint();
				Point p2 = jf_send.getLocation();
				p2.x += p1.x - p.x;
				p2.y += p1.y - p.y;
				jf_send.setLocation(p2);
			}
		});

		fileUpload.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				JFileChooser chooser = new JFileChooser();
				chooser.showOpenDialog(jf_send);
				File file = chooser.getSelectedFile();
				Date date = new Date(System.currentTimeMillis());
				SimpleDateFormat format = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss");
				String sendTime = format.format(date);

				String appendStr = sendTime.substring(11);
				String space1 = "";
				int len1 = appendStr.getBytes().length;
				for (int i = 5; i < 56 - len1; i++) {
					space1 += "  ";
				}
				String appendStr2 = "���ɹ������ļ�" + file.getName();
				String space2 = "";
				int len2 = appendStr2.getBytes().length;
				for (int i = 6; i < 56 - len2; i++) {
					space2 += "  ";
				}
				chatArea.setText(chatArea.getText() + "\n" + space1 + appendStr
						+ "\n" + space2 + appendStr2 + "\n");

				// ��������˷����ļ�
				MsgChatFile chatFile = new MsgChatFile();
				chatFile.setTotalLength((int) file.length() + 294);
				chatFile.setSrc(jkuser.getJknum());
				chatFile.setDest(destUser.getJknum());
				chatFile.setSendTime(sendTime);
				chatFile.setType(IMsgConstance.command_chatFile);
				chatFile.setFileName(file.getName());
				chatFile.setFileData(ImageUtil.getBytesFromFile(file));
				try {
					conn.sendMsg(chatFile);
				} catch (Exception e1) {
					e1.printStackTrace();
				}

			}
		});

		jf_send.setLocationRelativeTo(null);
		jf_send.setResizable(false);
		jf_send.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		jf_send.setVisible(true);
	}
}
