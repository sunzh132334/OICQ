package com.qq.msg;

/**
 * Ⱥ����Ϣ��
 * @author yy
 *
 */
public class MsgCommuChatText extends MsgHead {
	
	private int destCid;	//Ŀ��Ⱥ����
	private String sendTime;	//����ʱ��
	private String chatTxt;	//��������
	
	
	public int getDestCid() {
		return destCid;
	}
	public void setDestCid(int destCid) {
		this.destCid = destCid;
	}
	public String getSendTime() {
		return sendTime;
	}
	public void setSendTime(String sendTime) {
		this.sendTime = sendTime;
	}
	public String getchatTxt() {
		return chatTxt;
	}
	public void setchatTxt(String chatTxt) {
		this.chatTxt = chatTxt;
	}
	@Override
	public String toString() {
		return "MsgCommuChatText [destCid=" + destCid + ", sendTime="
				+ sendTime + ", chatTxt=" + chatTxt + "]";
	}

}
