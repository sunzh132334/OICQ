package com.qq.msg;

/**
 * Ⱥ��ɾ����Ϣ
 * @author yy
 *
 */
public class MsgDeleteCommunity extends MsgHead {
	
	private int cid;	//Ⱥ��id

	@Override
	public String toString() {
		return "MsgDeleteCommunity [cid=" + cid + "]";
	}

	public int getCid() {
		return cid;
	}

	public void setCid(int cid) {
		this.cid = cid;
	}
	

}
