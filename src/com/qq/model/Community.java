package com.qq.model;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * ��ӦȺ��ʵ��	����QQȺ
 * @author yy
 *
 */
public class Community implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int cid;	//Ⱥ����  Ⱥ����ʱΪ�����
	private String name;	//Ⱥ�������
	private int owner;	//Ⱥ��ӵ����
	private String des;	//Ⱥ��Ľ����Զ���
	private File iconpath;	//Ⱥ���ͷ��
	private List<Jkuser> userList = new ArrayList<Jkuser>() ;	//Ⱥ������г�Ա
	private List<Jkfile> fileList = new ArrayList<Jkfile>() ;	//Ⱥ�����е��ļ�����
	
	public List<Jkuser> getUserList() {
		return userList;
	}
	public void setUserList(List<Jkuser> userList) {
		this.userList = userList;
	}
	public List<Jkfile> getFileList() {
		return fileList;
	}
	public void setFileList(List<Jkfile> fileList) {
		this.fileList = fileList;
	}
	public List<Jkuser> getList() {
		return userList;
	}
	public void setList(List<Jkuser> list) {
		this.userList = list;
	}
	public int getCid() {
		return cid;
	}
	public void setCid(int cid) {
		this.cid = cid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getOwner() {
		return owner;
	}
	public void setOwner(int owner) {
		this.owner = owner;
	}
	public String getDes() {
		return des;
	}
	public void setDes(String des) {
		this.des = des;
	}
	public File getIconpath() {
		return iconpath;
	}
	public void setIconpath(File iconpath) {
		this.iconpath = iconpath;
	}
	public Community(int cid, String name, int owner, String des, File iconpath) {
		super();
		this.cid = cid;
		this.name = name;
		this.owner = owner;
		this.des = des;
		this.iconpath = iconpath;
	}
	public Community() {
		super();
	}
	@Override
	public String toString() {
		return "Community [cid=" + cid + ", name=" + name + ", owner=" + owner
				+ ", des=" + des + ", iconpath=" + iconpath + ", userList="
				+ userList + ", fileList=" + fileList + "]";
	}
	
	
	
	
	
}
