package com.qq.msg;

/**
 * �����ı���Ϣ��
 * @author yy
 *
 */
public class MsgChatText extends MsgHead {
	
	private String charTxt;	//������Ϣ���ı�����
	private String sendTime;
	
	public String getSendTime() {
		return sendTime;
	}

	public void setSendTime(String sendTime) {
		this.sendTime = sendTime;
	}

	public String getCharTxt() {
		return charTxt;
	}

	public void setCharTxt(String charTxt) {
		this.charTxt = charTxt;
	}

	@Override
	public String toString() {
		return "MsgChatText [charTxt=" + charTxt + "]";
	}

	
}
