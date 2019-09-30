package com.qq.client;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EventListener;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;


/**
 * �Զ���JList,ÿ��cell����һ��component,���component�Ϳ������ⶨ����,������һ��JPanel��չʾ
 * BaseList Demo ������
 * @ClassName BaseList
 * @author Jet
 * @date 2012-8-7
 */
public class BaseList extends JComponent{
	private static final long serialVersionUID = 1L;
	// list��Դ
	private ListSource source;

	public ListSource getSource() {
		return source;
	}
	/**
	 * ����BaseList��Դ
	 * @param source ListSource���Ͳ���
	 */
	public void setSource(ListSource source) {
		if (source == null) {
			return;
		} else {
			this.source.removeSourceRefreshListener(this);
			this.source = null;
		}
		this.source = source;
		this.source.addSourceRefreshListener(this);
		this.refreshData();
	}

	// ��ʾ�ؼ� ����һ���ӿ�
	private ListCellIface celliface;
	/**
	 * ����Ҫ��ʾ�Ŀؼ�
	 * @param cell
	 */
	public void setCellIface(ListCellIface cell) {
		this.celliface = cell;
	}

	// ���еĿؼ� �ڶԵ�ǰ�����ʱ���ô˱���
	private List<JComponent> totalcell = new ArrayList<JComponent>();

	// ѡ�е���ɫ
	private Color selectColor = new Color(252, 233, 161);

	public Color getSelectColor() {
		return selectColor;
	}

	public void setSelectColor(Color selectColor) {
		this.selectColor = selectColor;
	}

	// ��꾭������ɫ
	private Color passColor = new Color(196, 227, 248);

	public Color getPassColor() {
		return passColor;
	}

	public void setPassColor(Color passColor) {
		this.passColor = passColor;
	}

	// ѡ�е�����
	private int selectIndex = -1;

	/**
	 * ѡ��ĳһ��ʱִ�д˷���
	 * @param selectIndex
	 */
	public void setSelectIndex(int selectIndex) {
		for (int i = 0; i < totalcell.size(); i++) {
			//�����������ޱ���
			totalcell.get(i).setOpaque(false);
			totalcell.get(i).setBackground(null);
			if (celliface != null)
				((ListCellIface) totalcell.get(i)).setSelect(false);
		}
		if (selectIndex < totalcell.size() && selectIndex > -1) {
			//Ϊѡ�������ñ���
			totalcell.get(selectIndex).setOpaque(true);
			totalcell.get(selectIndex).setBackground(blist.getSelectColor());
			if (celliface != null)
				((ListCellIface) totalcell.get(selectIndex)).setSelect(true);
		}

		this.selectIndex = selectIndex;
	}

	public int getSelectIndex() {
		return selectIndex;
	}

	// ����
	private BaseList blist = this;

	public BaseList() {
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		source = new ListSource();
	}
	/**
	 * ˢ������
	 */
	public void refreshData() {
		if (source.getAllCell().size() != 0) {
			//����
			Collections.sort(source.getAllCell(), source.getComparator());
		}
		this.removeAll();
		this.totalcell.clear();
		for (int i = 0; i < source.getAllCell().size(); i++) {
			JComponent cell = null;
			if (celliface != null) {
				try {
					celliface = celliface.getClass().newInstance();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (celliface == null) {
				cell = new JLabel(source.getAllCell().get(i).toString());
				cell.setMaximumSize(new Dimension(ScreenUtil.getScreeWidth(),
						30));
				cell.setPreferredSize(new Dimension(0, 30));
				cell.setOpaque(false);
//				cell.setBackground(Color.orange);
			} else {
				cell = celliface.getListCell(this, source.getAllCell().get(i));
			}
			//������cell����������¼�
			cell.addMouseListener(new MouseAdapter() {
				//�����
				public void mouseClicked(MouseEvent e) {
					for (int i = 0; i < totalcell.size(); i++) {
						if (e.getSource().equals(totalcell.get(i))) {
							//��ǰѡ����
							blist.setSelectIndex(i);
							break;
						}
					}
				}
				//�������
				public void mouseEntered(MouseEvent e) {
					for (int i = 0; i < totalcell.size(); i++) {
						if (i != blist.getSelectIndex()) {
							//��ѡ����
							if (e.getSource().equals(totalcell.get(i))) {
								totalcell.get(i).setOpaque(true);
								totalcell.get(i).setBackground(blist.getPassColor());
								break;
							}
						}
					}
				}
				//����Ƴ�
				public void mouseExited(MouseEvent e) {
					JComponent jc = (JComponent) e.getSource();

					if (blist.getSelectIndex() < totalcell.size()) {
						if (blist.getSelectIndex() != -1) {
							if (!jc.equals(totalcell.get(blist.getSelectIndex()))) {
								//��ѡ����
								jc.setOpaque(false);
								jc.setBackground(null);
							}
						} else {
							jc.setOpaque(false);
							jc.setBackground(null);
						}
					}
				}
			});
			//��cell��������totalcell��,�ڴ�Ϊtotalcell��ֵ
			this.totalcell.add(cell);
			this.add(cell);
		}
		if (blist.getSelectIndex() != -1
				&& blist.getSelectIndex() < source.getAllCell().size()) {
			//Ϊѡ�������ñ���
			totalcell.get(blist.getSelectIndex())
					.setBackground(blist.getSelectColor());
		}
		this.revalidate();
		this.repaint();
	}

	/**
	 * Դ���ݸ���
	 * @param event
	 */
	public void sourceRefreshEvent(List event) {
		this.refreshData();
	}
}