package com.qq.msg;

import java.util.Arrays;

/**
 * �����ļ���Ϣ��
 * @author yy
 *
 */
public class MsgChatFile extends MsgHead {
	
	private String fileName;	//�ļ�����
	private byte[] fileData;	//�ļ�����
	private String sendTime;	//����ʱ��
	
	
	@Override
	public String toString() {
		return "MsgChatFile [fileName=" + fileName + ", sendTime=" + sendTime
				+ "]";
	}
	public String getSendTime() {
		return sendTime;
	}
	public void setSendTime(String sendTime) {
		this.sendTime = sendTime;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public byte[] getFileData() {
		return fileData;
	}
	public void setFileData(byte[] fileData) {
		this.fileData = fileData;
	}
	
}
