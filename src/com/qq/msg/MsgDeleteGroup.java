package com.qq.msg;


/**
 * ����ɾ����Ϣ
 * @author yy
 *
 */
public class MsgDeleteGroup extends MsgHead {
	
	private int gid;	//ɾ���ķ���id

	public int getGid() {
		return gid;
	}

	public void setGid(int gid) {
		this.gid = gid;
	}
	@Override
	public String toString() {
		return "MsgDeleteGroup [cid=" + gid + "]";
	}
}
