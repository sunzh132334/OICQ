package com.qq.client;

import javax.swing.JComponent;



/**
 * Cell�Ľӿ��࣬���ڹ���BaseList�е�cell
 * BaseList Demo ��Ҫ��
 * @ClassName ListCellIface
 * @author Jet
 * @date 2012-8-7
 */
public interface ListCellIface {
	public JComponent getListCell(BaseList list,Object value);
	public void setSelect(boolean iss);
}
