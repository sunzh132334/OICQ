package com.qq.client;

import java.io.File;

import com.qq.model.Community;
import com.qq.model.Jkgroup;
import com.qq.model.Jkuser;

/**
 * ��װ�ڵ������
 * @author yy
 *
 */
public class NodeData {
	
	public int nodeType;	//�ڵ������ 0�� 1Ⱥ 2���ߺ��� 3���ڵ�  4���ߺ���
	public Object value;	//���ݽڵ����;������������� 
	//������jkgroup ���� jkuser ���� community	�����Ǹ��ڵ�Ĵ��ַ�������
	public int onlineNum = 0;
	public int totalNum = 0;
	
	public File getIconFile() {
		if(nodeType == 0 || nodeType == 3) return null;
		else if(nodeType == 1) {
			Community community = (Community) value;
			return community.getIconpath();
		} else {
			Jkuser jkuser = (Jkuser) value;
			return jkuser.getIconpath();
		}
	}
	
	public NodeData(int nodeType, Object value) {
		this.nodeType = nodeType;
		this.value = value;
	}
	
	public NodeData(int nodeType, Object value, int onlineNum, int totalNum) {
		super();
		this.nodeType = nodeType;
		this.value = value;
		this.onlineNum = onlineNum;
		this.totalNum = totalNum;
	}

	@Override
	public String toString() {
		if(nodeType == 0) {
			Jkgroup group = (Jkgroup)value;
			return group.getName() + " " + onlineNum + "/" + totalNum;
		}else if(nodeType == 1) {
			Community community = (Community) value;
			return community.getName() + "(" + ((community.getDes() == null || community.getDes().equals(""))?"":community.getDes()) + ")";
		} else if(nodeType == 2 || nodeType == 4) {
			Jkuser user = (Jkuser) value;
			if(user.getSignature()!=null && !user.getSignature().equals(""))
				return user.getName() + "("  + user.getSignature() + ")";
			else 
				return user.getName() + "()";
		} else {
			return (String) value;
		}
	}
	

}
