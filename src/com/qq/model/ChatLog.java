package com.qq.model;

import java.io.Serializable;

/**
 * �����¼��
 * @author yy
 *
 */
public class ChatLog  implements Serializable {
	
	
	private int srcid;	//���ͷ�id	
	private int destid;	//���շ�id
	private String content;	//����
	private int state;	//״̬ 0δ����  1 �ѽ���
	private String sendTime;	//����ʱ��
	public int getSrcid() {
		return srcid;
	}
	public void setSrcid(int srcid) {
		this.srcid = srcid;
	}
	public int getDestid() {
		return destid;
	}
	public void setDestid(int destid) {
		this.destid = destid;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public String getSendtime() {
		return sendTime;
	}
	public void setSendtime(String sendTime) {
		this.sendTime = sendTime;
	}
	@Override
	public String toString() {
		return "ChatLog [srcid=" + srcid + ", destid=" + destid + ", content="
				+ content + ", state=" + state + ", sendTime=" + sendTime + "]";
	}
}
