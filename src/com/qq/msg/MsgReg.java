package com.qq.msg;

import java.io.Serializable;

import com.qq.model.Jkuser;

/**
 * ע��������Ϣ��
 * @author yy
 *
 */
public class MsgReg  extends MsgHead implements Serializable {
	
	private Jkuser jkuser;	//ע���û�����

	public Jkuser getJkuser() {
		return jkuser;
	}

	public void setJkuser(Jkuser jkuser) {
		this.jkuser = jkuser;
	}
	
	
	public MsgReg() {
		
	}
	
	public MsgReg(Jkuser jkuser) {
		this.jkuser = jkuser;
	}

	@Override
	public String toString() {
		return "MsgReg [jkuser=" + jkuser + "]";
	}
	
}
