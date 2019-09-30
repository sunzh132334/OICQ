package com.qq.model;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.qq.util.MD5Util;

/**
 * ��ӦJkuserʵ��
 * @author yy
 *
 */
public class Jkuser implements Serializable {
	
	
	private static final long serialVersionUID = 1L;
	private int jknum;	//�û�jknum
	private String name;	//�û��ǳ�
	private String password;	//�û�������
	private String signature;	//�û��ĸ���ǩ��
	private File iconpath;	//�û���ͷ��
	private String site;	//�û����ڵ�
	private String phone;	//�û��绰����
	private String email;	//�û�������
	private int state;	//�û���state 1 ���� 2 ����
	private String question;	//�ܱ�����
	private String answer;	//�ܱ��Ĵ�
	private int sex;	//�Ա�
	private List<Jkgroup> groupList;	//�����б�
	private List<Community> commuList;	//Ⱥ���б�
	private List<CommuChatLog> cmuChatLogList = new ArrayList<CommuChatLog>();	//δ�����Ⱥ����Ϣ��¼
	private List<ChatLog> logList = new ArrayList<ChatLog>();	//δ����������¼����
	private List<FriendApply> applyList = new ArrayList<FriendApply>();	//δ����ĺ������뼯��
	private List<Jkuser> uList = new ArrayList<Jkuser>();	//������Ⱥ�ĺ����б�
	private List<Community> cmuList = new ArrayList<Community>();	//������Ⱥ��Ⱥ�б�
	private List<Jkfile> fileList = new ArrayList<Jkfile>();	//δ���ܵ��ļ��б�
	private Map<Integer, Integer> hasNewFile = new HashMap<Integer, Integer>();	//cid--hasNewFileӳ�� 0�� 1��
	

	public Map<Integer, Integer> getHasNewFile() {
		return hasNewFile;
	}


	public void setHasNewFile(Map<Integer, Integer> hasNewFile) {
		this.hasNewFile = hasNewFile;
	}


	public List<Jkfile> getFileList() {
		return fileList;
	}


	public void setFileList(List<Jkfile> fileList) {
		this.fileList = fileList;
	}


	public List<Jkuser> getuList() {
		return uList;
	}


	public void setuList(List<Jkuser> uList) {
		this.uList = uList;
	}


	public List<Community> getCmuList() {
		return cmuList;
	}


	public void setCmuList(List<Community> cmuList) {
		this.cmuList = cmuList;
	}


	public List<CommuApply> getCmuApplyList() {
		return cmuApplyList;
	}


	public void setCmuApplyList(List<CommuApply> cmuApplyList) {
		this.cmuApplyList = cmuApplyList;
	}


	public List<CommuApplyResp> getCmuApplyRespList() {
		return cmuApplyRespList;
	}


	public void setCmuApplyRespList(List<CommuApplyResp> cmuApplyRespList) {
		this.cmuApplyRespList = cmuApplyRespList;
	}


	private List<FriendApplyResp> applyRespList = new ArrayList<FriendApplyResp>();	//δ����ĺ��Ѵ����Ӧ����
	private List<CommuApply> cmuApplyList = new ArrayList<CommuApply>();	//δ�����Ⱥ����
	private List<CommuApplyResp> cmuApplyRespList = new ArrayList<CommuApplyResp>();	//δ�����Ⱥ�����Ӧ��Ϣ
	
	
	public List<FriendApply> getApplyList() {
		return applyList;
	}


	public void setApplyList(List<FriendApply> applyList) {
		this.applyList = applyList;
	}


	public List<FriendApplyResp> getApplyRespList() {
		return applyRespList;
	}


	public void setApplyRespList(List<FriendApplyResp> applyRespList) {
		this.applyRespList = applyRespList;
	}


	public List<ChatLog> getLogList() {
		return logList;
	}


	public void setLogList(List<ChatLog> logList) {
		this.logList = logList;
	}


	public List<CommuChatLog> getCmuChatLogList() {
		return cmuChatLogList;
	}


	public void setCmuChatLogList(List<CommuChatLog> cmuChatLogList) {
		this.cmuChatLogList = cmuChatLogList;
	}


	public List<Jkgroup> getGroupList() {
		return groupList;
	}


	public void setGroupList(List<Jkgroup> groupList) {
		this.groupList = groupList;
	}


	public List<Community> getCommuList() {
		return commuList;
	}


	public void setCommuList(List<Community> commuList) {
		this.commuList = commuList;
	}


	public Jkuser() {
		super();
	}


	public Jkuser(int jknum, String name, String password, String signature,
			File iconpath, String site, String phone, String email,
			int state, String question, String answer,String sex) {
		super();
		this.jknum = jknum;
		this.name = name;
		this.password = MD5Util.MD5(password);
		this.signature = signature;
		this.iconpath = iconpath;
		this.site = site;
		this.phone = phone;
		this.email = email;
		this.state = state;
		this.question = question;
		this.answer = MD5Util.MD5(answer);
		if(sex.equals("��")) {
			this.sex = 1;
		}else if(sex.equals("Ů")) {
			this.sex = 2;
		}else {
			this.sex = 0;
		}
	}
	
	@Override
	public String toString() {
		return "Jkuser [jknum=" + jknum + ", name=" + name + ", password="
				+ password + ", signature=" + signature + ", iconpath="
				+ iconpath + ", site=" + site + ", phone=" + phone + ", email="
				+ email + ", state=" + state + ", question=" + question
				+ ", answer=" + answer + ", sex=" + sex + ", groupList="
				+ groupList + ", commuList=" + commuList + ", cmuChatLogList="
				+ cmuChatLogList + ", logList=" + logList + ", applyList="
				+ applyList + ", uList=" + uList + ", cmuList=" + cmuList
				+ ", fileList=" + fileList + " hasNewFile=" + hasNewFile + ", applyRespList="
				+ applyRespList + ", cmuApplyList=" + cmuApplyList
				+ ", cmuApplyRespList=" + cmuApplyRespList + "]";
	}


	public int getJknum() {
		return jknum;
	}
	public void setJknum(int jknum) {
		this.jknum = jknum;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getSignature() {
		return signature;
	}
	public void setSignature(String signature) {
		this.signature = signature;
	}
	public File getIconpath() {
		return iconpath;
	}
	public void setIconpath(File iconpath) {
		this.iconpath = iconpath;
	}
	public String getSite() {
		return site;
	}
	public void setSite(String site) {
		this.site = site;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public String getQuestion() {
		return question;
	}
	public void setQuestion(String question) {
		this.question = question;
	}
	public String getAnswer() {
		return answer;
	}
	public void setAnswer(String answer) {
		this.answer = answer;
	}


	public int getSex() {
		return sex;
	}


	public void setSex(int sex) {
		this.sex = sex;
	}
	
}
