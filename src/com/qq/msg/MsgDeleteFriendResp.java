package com.qq.msg;

/**
 * ɾ�����������Ӧ��Ϣ��
 * @author yy
 *
 */
public class MsgDeleteFriendResp extends MsgHead {
	private byte state;	//0ʧ��1�ɹ�
	private int gid = 0;
	
	public int getGid() {
		return gid;
	}

	public void setGid(int gid) {
		this.gid = gid;
	}

	public byte getState() {
		return state;
	}

	public void setState(byte state) {
		this.state = state;
	}

	@Override
	public String toString() {
		return "MsgDeleteFriendResp [state=" + state + ", gid=" + gid + "]";
	}

	
}
