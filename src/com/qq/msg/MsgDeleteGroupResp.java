package com.qq.msg;

/**
 * Ⱥ��ɾ����Ӧ��Ϣ
 * @author yy
 *
 */
public class MsgDeleteGroupResp extends MsgHead {
	
	private byte state;	//1�ɹ� 0ʧ��
	private int gid;

	public int getGid() {
		return gid;
	}

	public void setGid(int Gid) {
		this.gid = Gid;
	}

	public byte getState() {
		return state;
	}

	public void setState(byte state) {
		this.state = state;
	}

	@Override
	public String toString() {
		return "MsgDeleteGroupResp [state=" + state + ", Gid=" + gid + "]";
	}
}
