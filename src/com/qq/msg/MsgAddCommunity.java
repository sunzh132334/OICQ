package com.qq.msg;

/**
 * ��Ⱥ������Ϣ��
 * @author yy
 *
 */
public class MsgAddCommunity extends MsgHead {
	private int destCid;	//Ŀ��Ⱥ��id

	public int getDestCid() {
		return destCid;
	}
	public void setDestCid(int destCid) {
		this.destCid = destCid;
	}
	@Override
	public String toString() {
		return "MsgAddCommunity [destCid=" + destCid + "]";
	}
	
}
