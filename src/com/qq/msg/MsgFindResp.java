package com.qq.msg;

/**
 * ���������Ӧ��Ϣ��
 * @author yy
 *
 */
public class MsgFindResp extends MsgHead {
	
	private byte state;	//1:�ɹ� 0����

	public byte getState() {
		return state;
	}

	public void setState(byte state) {
		this.state = state;
	}

	@Override
	public String toString() {
		return "MsgFindResp [state=" + state + "]";
	}
	
}
