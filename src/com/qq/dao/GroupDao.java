package com.qq.dao;

public interface GroupDao {
	
	public void addFriends(int gid, int jknum);	//���������Ӻ���
	public int addGroup(String name, int jknum);	//��ӷ���
	public int deleteFriends(int jid, int gid);	//ɾ������
	public int getGidByJknum(int srcNum, int destNum);
	public int deleteGroup(int gid);	//ɾ��һ������
}
