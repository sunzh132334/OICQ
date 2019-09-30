package com.qq.client;

import java.util.Enumeration;
import java.util.List;
import java.util.Vector;



/**
 * ��������ģ��
 * ��Ҫ��������ˢ��
 * BaseList Demo ������
 * @ClassName BaseModel
 * @author Jet
 * @date 2012-8-7
 */

public class BaseModel {
	private Vector<BaseList> repository = new Vector<BaseList>();
	private BaseList bl;
	// ע����������������û��ʹ��Vector����ʹ��ArrayList��ôҪע��ͬ������
	public void addSourceRefreshListener(BaseList list) {
		repository.addElement(list);// �ⲽҪע��ͬ������
	}

	// �������û��ʹ��Vector����ʹ��ArrayList��ôҪע��ͬ������
	public void notifySourceRefreshEvent(List<Object> event) {
		Enumeration<BaseList> en = repository.elements();// �ⲽҪע��ͬ������
		while (en.hasMoreElements()) {
			bl = (BaseList) en.nextElement();
			bl.sourceRefreshEvent(event);
		}
	}
	// ɾ�����������������û��ʹ��Vector����ʹ��ArrayList��ôҪע��ͬ������
	public void removeSourceRefreshListener(BaseList srl) {
		repository.remove(srl);// �ⲽҪע��ͬ������
	}
}
