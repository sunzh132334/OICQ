package com.qq.msg;

/**
 * ������������Ӧ��Ϣ
 * @author yy
 *
 */
public class MsgAddGroupResp extends MsgHead {
	
	private byte state;	//0���ʧ�� 1��ӳɹ�

	public byte getState() {
		return state;
	}
	public void setState(byte state) {
		this.state = state;
	}
	@Override
	public String toString() {
		return "MsgAddGroupResp [state=" + state + "]";
	}
}
