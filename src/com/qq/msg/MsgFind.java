package com.qq.msg;

/**
 * ����������Ϣ��
 * @author yy
 *
 */
public class MsgFind  extends MsgHead {
	
	private byte classify;	//0 ���Һ��� 1 ����Ⱥ
	private int findId;	//����id Ⱥ�Ż���qq����
	
	public byte getClassify() {
		return classify;
	}
	public void setClassify(byte classify) {
		this.classify = classify;
	}
	public int getFindId() {
		return findId;
	}
	public void setFindId(int findId) {
		this.findId = findId;
	}
	@Override
	public String toString() {
		return "MsgFind [classify=" + classify + ", findId=" + findId + "]";
	}

	
}
