package com.qq.msg;

/**
 * ���������Ϣ��
 */
public class MsgAddGroup extends MsgHead {
	
	private String groupName;	//Ⱥ������

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	@Override
	public String toString() {
		return "MsgAddGroup [groupName=" + groupName + "]";
	}
}
