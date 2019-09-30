package com.qq.msg;

/**
 * ��½������Ϣ��
 * @author yy
 *
 */
public class MsgLogin extends MsgHead {
	
	private String password;	//��½����
	private int state;	//��½״̬ 1���� 2����

	public MsgLogin(String password,int state) {
		super();
		this.password = password;
		this.state = state;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public MsgLogin() {
		super();
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	@Override
	public String toString() {
		return "MsgLogin [password=" + password + ", state=" + state + "]";
	}

}
