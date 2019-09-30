package com.qq.client;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
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
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import com.qq.model.Community;
import com.qq.model.Jkgroup;
import com.qq.model.Jkuser;
import com.qq.msg.IMsgConstance;
import com.qq.msg.MsgHead;
import com.qq.util.ImageUtil;
import com.qq.util.OpenTree;

/**
 * Ⱥ��Ա��
 * @author yy
 *
 */
public class CommuMemberTree extends JTree {
	
	private static final long serialVersionUID = 1L;
	private Jkuser jkuser;
	private Community community;
	private DefaultMutableTreeNode root;	//���ĸ��ڵ�
	private Map<Integer, Integer> sendFrameMap = new HashMap<Integer, Integer>();	//<jknum,times>mapping
	Point p = null;
	private ClientConnection conn = ClientConnection.getIns();
	private List<JFrame> jfList = new ArrayList<JFrame>();
	private List<JTextArea> jtList = new ArrayList<JTextArea>();
	private Map<Integer, Integer> map = new HashMap<Integer, Integer>();
	private Jkuser destUser = null;
	
	public void setCommunity(Community commu) {
		this.community = community;
	}
	
	public CommuMemberTree(Community community,Jkuser jkuser) {
		this.community = community;
		this.jkuser = jkuser;
		go();
	}

	public void go() {
		root = new DefaultMutableTreeNode(new NodeData(
				3, "���ѹ���"));
		DefaultTreeModel tm = new DefaultTreeModel(root);
		tm.setAsksAllowsChildren(true);
		
		// ��ӷ���
		List<Jkuser> uList = community.getUserList();
		List<Jkuser> onUlist = new ArrayList<Jkuser>(); // ���ߺ����б�
		List<Jkuser> offUlist = new ArrayList<Jkuser>(); // �������б�
		int index = 0;
		for (int i = 0; i < uList.size(); i++) {
			if(uList.get(i).getJknum() == community.getOwner()) {
				uList.get(i).setName("(Ⱥ��)"+uList.get(i).getName());
				index = i;
				if(uList.get(i).getState() == 1) {
					onUlist.add(uList.get(i));
				}else {
					offUlist.add(uList.get(i));
				}
			}
		}
		for (int j = 0; j < uList.size(); j++) {
			if(j == index) continue;
			if(uList.get(j).getState() == 1) {
				onUlist.add(uList.get(j));
			}else {
				offUlist.add(uList.get(j));
			}
		}
		DefaultMutableTreeNode admin = new DefaultMutableTreeNode(new NodeData(3, "Ⱥ��Ա   " + onUlist.size()+"/" + uList.size()));
		root.add(admin);
		for (int j = 0; j < onUlist.size(); j++) {
			DefaultMutableTreeNode node = new DefaultMutableTreeNode(new NodeData(2, onUlist.get(j)));
			node.setAllowsChildren(false);
			admin.add(node);
		}
		for (int i = 0; i < offUlist.size(); i++) {
			DefaultMutableTreeNode node = new DefaultMutableTreeNode(new NodeData(4, offUlist.get(i)));
			node.setAllowsChildren(false);
			admin.add(node);
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
		OpenTree.expandTree(this, true);
		final JPopupMenu jPopupMenu = new JPopupMenu();
		JMenuItem addFriend = new JMenuItem("��Ӻ���");
		jPopupMenu.add(addFriend);
		JMenuItem selectUser = new JMenuItem("�鿴������Ϣ");
		jPopupMenu.add(selectUser);
		
		// ��������Mouse�¼���������˫������������û�����Ϣ
		this.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {// ˫���¼�
					showSendFrame();// ����������Ϣ��
				}
			}
			
			@Override
			public void mouseReleased(MouseEvent e) {
				if(e.isPopupTrigger()) {
					TreePath tp = CommuMemberTree.this.getSelectionPath();
					if (tp == null) {// δѡ�����ڵ�
						return;
					}
					Object obj = tp.getLastPathComponent();// ȡ��ѡ�еĽڵ�
					DefaultMutableTreeNode node = (DefaultMutableTreeNode) obj;
					Object userO = node.getUserObject();// ȡ�ýڵ��ڵĶ���
					if ((userO instanceof NodeData)
							&& (((NodeData) userO).nodeType == 2 || ((NodeData) userO).nodeType == 4)) {// ѡ�е���һ���û��ڵ����
						NodeData data = (NodeData) userO;
						destUser = (Jkuser) data.value;
						// ����������Ϣ��
						jPopupMenu.show(CommuMemberTree.this,e.getX(),e.getY());
					}
				}
			}
		});
		
		
		addFriend.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				//���Ȳ��Һ����б����Ƿ��Ѿ��иú���
				int num = destUser.getJknum();
				boolean flag = false;
				boolean dump = false;
				List<Jkgroup> gList = jkuser.getGroupList();
				for (int i = 0; i < gList.size(); i++) {
					List<Jkuser> ul = gList.get(i).getUserList();
					for (int j = 0; j < ul.size(); j++) {
						if(ul.get(j).getJknum() == num) {
							flag = true;
							dump = true;
						}
					}
					if(dump) {
						break;
					}
				}
				
				if(flag) {
					JOptionPane.showConfirmDialog(CommuMemberTree.this, "���Ѿ���Ӹú���,�벻Ҫ�ظ����!");
					return;
				}
				
				MsgHead head = new MsgHead();
				head.setTotalLength(13);
				head.setType(IMsgConstance.command_addFriend);
				head.setSrc(jkuser.getJknum());
				head.setDest(destUser.getJknum());
				
				
				try {
					conn.sendMsg(head);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				
				JOptionPane.showConfirmDialog(CommuMemberTree.this, "�����������"+destUser.getName()+"("+destUser.getJknum()+")"+"Ϊ����");
				}
		});
		
		selectUser.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
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
				jl_bg.setBounds(0, 0, bg_icon.getIconWidth(), bg_icon.getIconHeight());
				// �����ݴ���ת��ΪJPanel���������÷���setOpaque()��ʹ���ݴ���͸��
				JPanel imgPanel = (JPanel) frame.getContentPane();
				imgPanel.setOpaque(false);
				frame.getLayeredPane().setLayout(null);
				frame.getLayeredPane().add(jl_bg, new Integer(Integer.MIN_VALUE));
				// ����imagePane�Ĳ��ַ�ʽΪ���Բ���
				imgPanel.setLayout(null);
				
				JLabel jl_jknum = new JLabel("QQ����:");
				jl_jknum.setBounds(40,20,60,30);
				JLabel jl_jknum2 = new JLabel(destUser.getJknum()+"");
				jl_jknum2.setBounds(110,20,85,30);
				JLabel jl_name = new JLabel("�ǳ�:");
				jl_name.setBounds(40,60,60,30);
				JLabel jl_name2 = new JLabel(destUser.getName());
				jl_name2.setBounds(110, 60, 260, 30);
				JLabel jl_sign = new JLabel("����ǩ��:");
				jl_sign.setBounds(40, 100, 60, 30);
				JLabel jl_sign2 = new JLabel(destUser.getSignature());
				jl_sign2.setBounds(110,100,260,30);
				JLabel jl_site = new JLabel("�ص�:");
				jl_site.setBounds(40,140,60,30);
				JLabel jl_site2 = new JLabel(destUser.getSite());
				jl_site2.setBounds(110,140,85,30);
				JLabel jl_phone = new JLabel("�绰:");
				jl_phone.setBounds(40,180,60,30);
				JLabel jl_phone2 = new JLabel(destUser.getPhone());
				jl_phone2.setBounds(110,180,85,30);
				JLabel jl_email = new JLabel("����:");
				jl_email.setBounds(40,220,60,30);
				JLabel jl_email2 = new JLabel(destUser.getEmail());
				jl_email2.setBounds(110,220,260,30);
				JLabel jl_sex = new JLabel("�Ա�:");
				jl_sex.setBounds(40, 260, 60, 30);
				String sex = (destUser.getSex() == 0)?"��":"Ů";
				JLabel jl_sex2 = new JLabel(sex);
				jl_sex2.setBounds(110,260,85,30);
				
				//������
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
		});
		
	}

	protected void showSendFrame() {
		// �õ�����ѡ�еĽڵ�:
		TreePath tp = this.getSelectionPath();
		if (tp == null) {// δѡ�����ڵ�
			return;
		}
		Object obj = tp.getLastPathComponent();// ȡ��ѡ�еĽڵ�
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) obj;
		Object userO = node.getUserObject();// ȡ�ýڵ��ڵĶ���
		if ((userO instanceof NodeData)
				&& (((NodeData) userO).nodeType == 2 || ((NodeData) userO).nodeType == 4)) {// ѡ�е���һ���û��ڵ����
			NodeData data = (NodeData) userO;
			final Jkuser destUser = (Jkuser) data.value;
			// ����������Ϣ��
			if(sendFrameMap.get(destUser.getJknum()) == null) {
				showSendMsgUI(destUser);
				sendFrameMap.put(destUser.getJknum(), 1);
			}
		}
	}

	private void showSendMsgUI(final Jkuser destUser) {
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

		
		
		jf_send.setLocationRelativeTo(null);
		jf_send.setResizable(false);
		jf_send.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		jf_send.setVisible(true);
	}
	
	
}
