package com.qq.model;

import java.io.File;
import java.io.Serializable;


/**
 * ��Ӧ���ݿ��е��ļ�ʵ��
 * @author yy
 *
 */
public class Jkfile implements Serializable, Comparable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int fid;	//Ψһ�ı�ʶһ���ļ�
	private File file;	//�ļ�
	private String filename;	//�ļ�����
	private int uid;	//�ļ��ķ�����
	private String sendTime;	//�ļ��ķ���ʱ��
	
	public String getSendTime() {
		return sendTime;
	}
	public void setSendTime(String sendTime) {
		this.sendTime = sendTime;
	}
	public int getFid() {
		return fid;
	}
	public void setFid(int fid) {
		this.fid = fid;
	}
	public File getFile() {
		return file;
	}
	public void setFile(File file) {
		this.file = file;
	}
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	public int getUid() {
		return uid;
	}
	public void setUid(int uid) {
		this.uid = uid;
	}
	public Jkfile(int fid, File file, String filename, int uid) {
		super();
		this.fid = fid;
		this.file = file;
		this.filename = filename;
		this.uid = uid;
	}
	public Jkfile() {
		super();
	}
	@Override
	public String toString() {
		return "Jkfile [fid=" + fid + ", filename="
				+ filename + ", uid=" + uid + "]";
	}
	@Override
	public int compareTo(Object o) {
		Jkfile file = (Jkfile) o;
		if(this.sendTime.compareTo(file.getSendTime()) < 0) {
			return 1;
		}else if(this.sendTime.compareTo(file.getSendTime()) == 0) {
			if(this.uid < file.getUid()) return 1;
			else return 0;
		}else {
			return -1;
		}
	}

	
	
	
	
	
}
