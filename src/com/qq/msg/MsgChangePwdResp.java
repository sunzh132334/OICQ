package com.qq.msg;

/**
 * �޸������Ӧ��Ϣ
 * @author yy
 *
 */
public class MsgChangePwdResp extends MsgHead {
	private byte state;	//0�޸�ʧ�� 1�޸ĳɹ�

	public byte getState() {
		return state;
	}

	public void setState(byte state) {
		this.state = state;
	}

	@Override
	public String toString() {
		return "MsgChangePwdResp [state=" + state + "]";
	}
}
