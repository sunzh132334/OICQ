package com.qq.util;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

import com.qq.client.CommunityTree;
import com.qq.model.ChatLog;
import com.qq.model.CommuChatLog;
import com.qq.model.Jkfile;


/**
 * ���ߵ���������
 *
 */
public class OffMsgInfoUtil {
	private Point oldP;										// ��һ������,�϶�����ʱ��
	private TipWindow tw = null;							// ��ʾ��
	private ImageIcon img = null;							// ͼ�����
	private JLabel imgLabel = null; 						// ����ͼƬ��ǩ
	private JPanel headPan = null;
	private JPanel feaPan = null;
	private JPanel btnPan = null;
	private JLabel head = null;								// ��ɫ����
	private JLabel close = null;							// �رհ�ť
	private JTextArea feature = null;						// ����
	private JScrollPane jfeaPan = null;
	public JLabel sure = null;
	private String titleT = null;
	private String word = null;
	private String time = null;
	private ArrayList<ChatLog> cList;
	private ArrayList<CommuChatLog> cmuChatLog;
	private CommunityTree communityTree;
	private List<Jkfile> fileList = new ArrayList<Jkfile>();	//δ������ļ��б�
	
	public OffMsgInfoUtil(List<ChatLog> cList,List<CommuChatLog> cmuChatLog, CommunityTree communityTree,
			ArrayList<Jkfile> fileList) {
		this.cList = (ArrayList<ChatLog>) cList;
		this.cmuChatLog = (ArrayList<CommuChatLog>) cmuChatLog;
		this.communityTree = communityTree;
		this.fileList = fileList;
	}
	
	
	public void init() {
		// �½�300x220����Ϣ��ʾ��
		tw = new TipWindow(300, 220);
		img = new ImageIcon("background.gif");
		imgLabel = new JLabel(img);
		// ���ø������Ĳ����Լ�����пؼ��ı߽�
		headPan = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
		feaPan = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
		btnPan = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
		head = new JLabel(titleT);
		close = new JLabel(" x");
		feature = new JTextArea(word);
		jfeaPan = new JScrollPane(feature);
		sure = new JLabel("�鿴������Ϣ");
		sure.setHorizontalAlignment(SwingConstants.CENTER);

		// �������������Ϊ͸�������򿴲�������ͼƬ
		((JPanel) tw.getContentPane()).setOpaque(false);
		headPan.setOpaque(false);
		feaPan.setOpaque(false);
		btnPan.setOpaque(false);

		// ����JDialog����������ͼƬ
		tw.getLayeredPane().add(imgLabel, new Integer(Integer.MIN_VALUE));
		imgLabel.setBounds(0, 0, img.getIconWidth(), img.getIconHeight());
		headPan.setPreferredSize(new Dimension(300, 60));

		// ������ʾ��ı߿�,��Ⱥ���ɫ
		tw.getRootPane().setBorder(
				BorderFactory.createMatteBorder(1, 1, 1, 1, Color.gray));

		close.setFont(new Font("Arial", Font.BOLD, 15));
		close.setPreferredSize(new Dimension(20, 20));
		close.setVerticalTextPosition(JLabel.CENTER);
		close.setHorizontalTextPosition(JLabel.CENTER);
		close.setCursor(new Cursor(12));
		close.setToolTipText("�ر�");

		head.setPreferredSize(new Dimension(250, 35));
		head.setVerticalTextPosition(JLabel.CENTER);
		head.setHorizontalTextPosition(JLabel.CENTER);
		head.setFont(new Font("����", Font.PLAIN, 12));
		head.setForeground(Color.blue);

		feature.setEditable(false);
		feature.setForeground(Color.red);
		feature.setFont(new Font("����", Font.PLAIN, 13));
		feature.setBackground(new Color(184, 230, 172));
		// �����ı����Զ�����
		feature.setLineWrap(true);

		jfeaPan.setPreferredSize(new Dimension(250, 80));
		jfeaPan.setBorder(null);
		jfeaPan.setBackground(Color.black);

		// Ϊ�������ı��򣬼Ӹ��յ�JLabel������������ȥ
		JLabel jsp = new JLabel();
		jsp.setPreferredSize(new Dimension(300, 25));

		sure.setPreferredSize(new Dimension(110, 30));
		// ���ñ�ǩ�������
		sure.setCursor(new Cursor(12));
		headPan.add(close);
		headPan.add(head);

		feaPan.add(jsp);
		feaPan.add(jfeaPan);


		btnPan.add(sure);

		tw.add(headPan, BorderLayout.NORTH);
		tw.add(feaPan, BorderLayout.CENTER);
		tw.add(btnPan, BorderLayout.SOUTH);
	}

	public void handle() {
		// Ϊ���°�ť������Ӧ���¼�
		sure.addMouseListener(new MouseAdapter() {
			
			public void mouseEntered(MouseEvent e) {
				sure.setBorder(BorderFactory.createLineBorder(Color.gray));
			}
			public void mouseExited(MouseEvent e) {
				sure.setBorder(null);
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				
				//չʾ���е�������Ϣ
				final JFrame frame = new JFrame("������Ϣ��ʾ��");
				frame.setBounds(0,0,500,400);
				// ���ñ���ͷ��
				ImageIcon icon = new ImageIcon("./images/logo.jpg");
				Image logo = icon.getImage();
				frame.setIconImage(logo);
				// ���ñ���ͼƬ
				ImageIcon bg_icon = new ImageIcon("./images/reg_bg1.jpg");
				JLabel jl_bg = new JLabel(bg_icon);
				jl_bg.setBounds(0, 0, bg_icon.getIconWidth(), bg_icon.getIconHeight());
				// �����ݴ���ת��ΪJPanel���������÷���setOpaque()��ʹ���ݴ���͸��
				JPanel imgPanel = (JPanel) frame.getContentPane();
				imgPanel.setOpaque(false);
				frame.getLayeredPane().setLayout(null);
				frame.getLayeredPane().add(jl_bg, new Integer(Integer.MIN_VALUE));
				// ����imagePane�Ĳ��ַ�ʽΪ���Բ���
				imgPanel.setLayout(null);
				
				final ArrayList<String> strList = new ArrayList<String>();
				
				for (int i = 0; i < cList.size(); i++) {
					ChatLog chatLog = cList.get(i);
					String str = chatLog.getSrcid() + " �� " + chatLog.getSendtime().trim() + "  ����˵:" + chatLog.getContent();
					strList.add(str);
				}
				
				for (int j = 0; j < fileList.size(); j++) {
					Jkfile file = fileList.get(j);
					String str = file.getUid()+" �� "+file.getSendTime().trim().substring(5)+" �����ļ� "+file.getFilename().trim()+"    (������˫��)";
					strList.add(str);
				}
		
				final JList list = new JList(strList.toArray());
				final JScrollPane pane = new JScrollPane(list,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
				pane.setBounds(0,0,500,18*(cList.size()+fileList.size()));
				JButton btn = new JButton("�鿴����δ�����Ⱥ��Ϣ");
				btn.setBounds(140,18*cList.size()+30,220,30);
				
				
				list.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						if(e.getClickCount() == 2) {
							JList list2 = (JList) e.getSource();
							int index = list2.locationToIndex(e.getPoint());
							if(index >= cList.size()) {
								index-=cList.size();
								Jkfile jkfile = fileList.get(index);
								File file = jkfile.getFile();
								JFileChooser chooser = new JFileChooser();
								chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
								File file2 = new File("G:/");
								chooser.setCurrentDirectory(file2);
								int num = chooser.showSaveDialog(chooser);
								if(num == chooser.APPROVE_OPTION) {
									String path = chooser.getSelectedFile().toString()+"/"+jkfile.getFilename();
									FileOutputStream fos = null;
									FileInputStream fins = null;
									try {
										fos = new FileOutputStream(path);
										fins = new FileInputStream(file);
										byte[] data = new byte[1024];
										while(fins.read(data) != -1) {
											fos.write(data);
											fos.flush();
										}
									} catch (FileNotFoundException e1) {
										e1.printStackTrace();
									} catch (IOException e1) {
										e1.printStackTrace();
									}finally {
										if(fos!=null) {
											try {
												fos.close();
											} catch (IOException e1) {
												e1.printStackTrace();
											}
										}
										if(fins!= null) {
											try {
												fins.close();
											} catch (IOException e1) {
												e1.printStackTrace();
											}
										}
									}
								}
							}
							
							String s = strList.get(index);
							strList.remove(s);
							frame.remove(pane);
							JList list1 = new JList(strList.toArray());
							JScrollPane pane = new JScrollPane(list1,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
							pane.setBounds(0,0,500,18*(strList.size()));
							frame.add(pane);
							pane.updateUI();
							frame.repaint();
						}
					}
				});
				
				
				btn.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
					
						communityTree.precessCommuMsg(cmuChatLog);
					}
				});
				
				
				frame.add(btn);
				frame.add(pane);
				frame.setResizable(false);
				frame.setLocationRelativeTo(null);
				frame.setLayout(null);
				frame.setVisible(true);
				frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				
				
				
				
				
				tw.dispose();
			}
			
		});
		
		// ���Ͻǹرհ�ť�¼�
		close.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				tw.close();
			}
			public void mouseEntered(MouseEvent e) {
				close.setBorder(BorderFactory.createLineBorder(Color.gray));
			}
			public void mouseExited(MouseEvent e) {
				close.setBorder(null);
			}
		});
	}

	public void show(String titleT,String word){
		this.titleT = titleT;
		this.word = word;
		init();
		handle();
		tw.setAlwaysOnTop(true);
		tw.setUndecorated(true);
		tw.setResizable(false);
		tw.setVisible(true);
		tw.run();
	}
}

class TipWindow extends JDialog {
	private static final long serialVersionUID = 8541659783234673950L;
	private static Dimension dim;
	private int x, y;
	private int width, height;
	private static Insets screenInsets;

	public TipWindow(int width, int height) {
		this.width = width;
		this.height = height;
		dim = Toolkit.getDefaultToolkit().getScreenSize();
		screenInsets = Toolkit.getDefaultToolkit().getScreenInsets(
				this.getGraphicsConfiguration());
		x = (int) (dim.getWidth() - width - 3);
		y = (int) (dim.getHeight() - screenInsets.bottom - 3);
		initComponents();
	}

	public void run() {
		for (int i = 0; i <= height; i += 10) {
			try {
				this.setLocation(x, y - i);
				Thread.sleep(5);
			} catch (InterruptedException ex) {
			}
		}
//		// �˴���������ʵ������Ϣ��ʾ��5����Զ���ʧ
//		while(true) {}
	}

	private void initComponents() {
		this.setSize(width, height);
		this.setLocation(x, y);
		this.setBackground(Color.gray);
	}

	public void close() {
		x = this.getX();
		y = this.getY();
		int ybottom = (int) dim.getHeight() - screenInsets.bottom;
		for (int i = 0; i <= ybottom - y; i += 10) {
			try {
				setLocation(x, y + i);
				Thread.sleep(5);
			} catch (InterruptedException ex) {
			}
		}
		dispose();
	}

}