package com.qq.msg;

/**
 * Ⱥ�鴴����Ӧ��Ϣ
 * @author yy
 *
 */
public class MsgCreateCommunityResp extends MsgHead {
	
	private byte state;	//0ʧ��1�ɹ�
	private int cid;	//Ⱥ����
	
	@Override
	public String toString() {
		return "MsgCreateCommunityResp [state=" + state + ", cid=" + cid + "]";
	}
	public byte getState() {
		return state;
	}
	public void setState(byte state) {
		this.state = state;
	}
	public int getCid() {
		return cid;
	}
	public void setCid(int cid) {
		this.cid = cid;
	}
	
	
	
}
