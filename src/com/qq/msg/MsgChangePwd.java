package com.qq.msg;

/**
 * �޸�����������Ϣ
 * @author yy
 *
 */
public class MsgChangePwd extends MsgHead {
	private String newPwd;	//�µ�����

	public String getNewPwd() {
		return newPwd;
	}

	public void setNewPwd(String newPwd) {
		this.newPwd = newPwd;
	}
	@Override
	public String toString() {
		return "MsgChangePwd [newPwd=" + newPwd + "]";
	}
}
