package com.qq.model;

import java.io.Serializable;

/**
 * Ⱥ��Ϣ��¼��Ӧ��ʵ��
 * @author yy
 *
 */
public class CommuChatLog implements Serializable {
	
	private int cid;	//Ⱥid
	private int srcid;	//�����ߵ�jknum
	private String content;	//���͵�����
	private int lid;	//Ψһ��ʾһ��Ⱥ�ļ�¼
	private String sendTime;	//��Ϣ�ķ���ʱ��
	public int getCid() {
		return cid;
	}
	public void setCid(int cid) {
		this.cid = cid;
	}
	public int getSrcid() {
		return srcid;
	}
	public void setSrcid(int srcid) {
		this.srcid = srcid;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public int getLid() {
		return lid;
	}
	public void setLid(int lid) {
		this.lid = lid;
	}
	public String getSendTime() {
		return sendTime;
	}
	public void setSendTime(String sendTime) {
		this.sendTime = sendTime;
	}
	@Override
	public String toString() {
		return "CommuChatLog [cid=" + cid + ", srcid=" + srcid + ", content="
				+ content + ", lid=" + lid + ", sendTime=" + sendTime + "]";
	}
	
	

}
