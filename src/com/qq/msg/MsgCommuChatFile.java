package com.qq.msg;

import java.util.Arrays;

/**
 * Ⱥ���ļ���Ϣ��
 * @author yy
 *
 */
public class MsgCommuChatFile extends MsgHead {
	private String sendTime;	//�ļ��ķ���ʱ��
	private byte[] fileData;	//�ļ�������
	private int destCid;	//Ŀ��Ⱥ����
	private String fileName;	//�ļ�����
	
	
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getSendTime() {
		return sendTime;
	}
	public void setSendTime(String sendTime) {
		this.sendTime = sendTime;
	}
	public byte[] getFileData() {
		return fileData;
	}
	public void setFileData(byte[] fileData) {
		this.fileData = fileData;
	}
	public int getDestCid() {
		return destCid;
	}
	public void setDestCid(int destCid) {
		this.destCid = destCid;
	}
	@Override
	public String toString() {
		return "MsgCommuChatFile [sendTime=" + sendTime +  ", destCid=" + destCid
				+ ", fileName=" + fileName + "]";
	}

}
