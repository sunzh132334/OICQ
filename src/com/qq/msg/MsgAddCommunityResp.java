package com.qq.msg;

/**
 * ��Ⱥ�����Ӧ��Ϣ��
 * @author yy
 *
 */
public class MsgAddCommunityResp extends MsgHead {

	private int res;	//1ͬ�� 0�ܾ�
	private int destcid;	//Ŀ�������qqȺ����
	
	
	
	public int getDestcid() {
		return destcid;
	}
	public void setDestcid(int destcid) {
		this.destcid = destcid;
	}
	public int getRes() {
		return res;
	}
	public void setRes(int res) {
		this.res = res;
	}
	@Override
	public String toString() {
		return "MsgAddCommunityResp [res=" + res + ", destcid=" + destcid + "]";
	}
	

}
