package com.qq.msg;

/**
 * Ⱥ��ɾ����Ӧ��Ϣ
 * @author yy
 *
 */
public class MsgDeleteCommunityResp extends MsgHead {
	
	private byte state;	//1�ɹ�0ʧ��
	private int cid;	//ɾ����Ⱥ��id
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
	@Override
	public String toString() {
		return "MsgDeleteCommunityResp [state=" + state + ", cid=" + cid + "]";
	}
}
