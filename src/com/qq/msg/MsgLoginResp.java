package com.qq.msg;


import com.qq.model.Jkuser;

//��½����Ӧ����Ϣ��
public class MsgLoginResp  extends MsgHead {
	
	private byte state;	//0:��½success  ������ʧ��

	@Override
	public String toString() {
		return "MsgLoginResp [state=" + state + "]";
	}

	public byte getState() {
		return state;
	}

	public void setState(byte state) {
		this.state = state;
	}
	

}
