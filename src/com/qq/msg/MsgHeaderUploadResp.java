package com.qq.msg;

/**
 * ͷ���ϴ���Ӧ��Ϣ��
 * @author yy
 *
 */
public class MsgHeaderUploadResp extends MsgHead {
	
	
	private byte state;	//1:�ɹ�  ����:ʧ��

	public byte getState() {
		return state;
	}

	public void setState(byte state) {
		this.state = state;
	}

	@Override
	public String toString() {
		return "MsgHeaderUploadResp [state=" + state + "]";
	}
	
}
