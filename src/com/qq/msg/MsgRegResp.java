
package com.qq.msg;

import com.qq.model.Jkuser;

/**
 * ע����ϢӦ����
 * @author yy
 *
 */
public class MsgRegResp extends MsgHead {
	
	private byte state;	//0:ע��ɹ�  ����:ע��error
	private int jknum;	//ע��ɹ����ص�jknum
	
	
	public byte getState() {
		return state;
	}
	public void setState(byte state) {
		this.state = state;
	}
	public int getJknum() {
		return jknum;
	}
	public void setJknum(int jknum) {
		this.jknum = jknum;
	}
	
	public MsgRegResp(byte state, int jknum) {
		super();
		this.state = state;
		this.jknum = jknum;
	}
	@Override
	public String toString() {
		return "MsgRegResp [state=" + state + ", jknum=" + jknum + "]";
	}


}
